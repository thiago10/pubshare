package br.ufrn.dimap.pubshare.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufrn.dimap.pubshare.domain.Article;

/**
 * Parser to process ACM results.
 * @author itamir
 *
 */
public class ACMParser extends Parser{

	@Override
	public List<Article> findArticlesByTitle(String title) {
		List<Article> articles = new ArrayList<Article>();
		try {
			URL url = new URL("http://dl.acm.org/results.cfm?query=android&querydisp="+title+"&source_query=&start=11&srt=score%20dsc" +
					"&short=0&source_disp=&since_month=&since_year=&before_month=&before_year=&coll=DL&dl=GUIDE&termshow=matchall");
			parseUrl(articles, url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return articles;
	}

	@Override
	public List<Article> findArticleByAuthor(String author) {
		List<Article> articles = new ArrayList<Article>();
		try {
			URL url = new URL("?");
			parseUrl(articles, url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return articles;
	}

	@Override
	protected void parseUrl(List<Article> articles, URL url) throws IOException {
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Host", "dl.acm.org");
		connection.setRequestProperty("Connection", "close");
		connection.setRequestProperty("User-Agent", "gzip");
		connection.setRequestProperty("Accept-Encoding", "dl.acm.org");
		connection.setRequestProperty("Referer", "www.ufrn.br");
		

		BufferedReader input = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		
		
		String linha = input.readLine();
		
	    //processando artigos
		boolean chegouAoFim = false;
		while(!chegouAoFim) {
			
			Article article = new Article();
			
			linha = input.readLine();
			
			//titulo
			if(linha.contains("citation.cfm?")) 
				article.setTitle(replaceSpecialText(linha.trim()));
			
			//autores
			if(linha.contains("author_page.cfm?")) 
				article.getAuthors().add(replaceSpecialText(linha.trim()));
			
			if(linha.contains("authors")) {
				input.readLine(); // pulando uma linha
				linha = input.readLine();
				if(!linha.trim().equals(""))
					article.getAuthors().add(linha.trim());
			}
			
			//abstract
			if(linha.contains("abstract2")) {
				input.readLine(); // pulando uma linha
				input.readLine(); // pulando uma linha
				input.readLine(); // pulando uma linha
				linha = input.readLine();
				article.setAbztract(replaceSpecialText(linha.trim()));
			}
			
			//conferencia e ano
			String event = "";
			if(linha.contains("<td class=\"small-text\" nowrap>")) {
				input.readLine(); // pulando uma linha
				linha = input.readLine();
				event += linha.trim();
			}
			
			if(linha.contains("addinfo")) {
				linha = input.readLine();
				event = replaceSpecialText(linha.trim()) + event;
			}
			article.setEventInformation(event);
			
			//link para download
			if(linha.contains("ft_gateway.cfm")) {
				article.setDownloadLink(urlDownloadExtract("http://dl.acm.org/"+linha.trim()));
			}

			articles.add(article);
			
			if(linha.contains("</html>"))
				chegouAoFim=true;
		}
		
	}

	@Override
	protected String replaceSpecialText(String text) {
		return text.replaceAll("<[^>]*>", "");
	}

	@Override
	protected String urlDownloadExtract(String text) {
		return text.replace("<A HREF=\"", "").replace("title=\"PDF\" target=\"_blank\">", "");
	}
}
