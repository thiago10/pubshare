package br.ufrn.dimap.pubshare.activity;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.ufrn.dimap.pubshare.adapters.ArticlesDownloadedListAdapter;
import br.ufrn.dimap.pubshare.domain.ArticleDownloaded;
import br.ufrn.dimap.pubshare.mocks.ArticlesDownloadedMockFactory;

public class ArticlesDownloadedActivity extends Activity {

	private ArrayAdapter<ArticleDownloaded> adapter;
	private ListView downloadsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articles_downloaded);		
		configureActionBar();	
		List<ArticleDownloaded> downloads = ArticlesDownloadedMockFactory.makeArticleDownloadedList();
		configureListView(downloads);	
	}
	
	private void configureActionBar() {
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	        ActionBar actionBar = getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);	        
	    }
	}

	private void configureListView( List<ArticleDownloaded>  downloads) {
		
		adapter = new ArticlesDownloadedListAdapter(this, R.layout.row_listview_article_downloaded_list , downloads);
		downloadsListView = (ListView) findViewById(R.id.list_view_articles_downloaded);
		if ( downloadsListView == null ){
			Log.d(this.getClass().getSimpleName(), "Cant find R.layout.row_listview_article_list");
		}
		downloadsListView.setAdapter( adapter );
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		
	    MenuItem menuItem = (MenuItem) menu.findItem( R.id.menu_my_downloads );
	    menuItem.setVisible( false );	  
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, ArticleListActivity.class);
	            // See Using the App Icon for Navigation ref: http://developer.android.com/guide/topics/ui/actionbar.html
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
 
	}

}
