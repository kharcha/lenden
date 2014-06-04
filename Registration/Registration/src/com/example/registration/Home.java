package com.example.registration;

import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class Home extends TabActivity {
    /** Called when the activity is first created. */
	String email;
	
    
	/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
 
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
 
        return super.onCreateOptionsMenu(menu);
    }*/
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        TabHost tabHost = getTabHost();
        email = getIntent().getExtras().getString("email");
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("Friend List");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Friend List", getResources().getDrawable(R.drawable.ic_launcher));
        Intent photosIntent = new Intent(this, frndlist.class);
        photosIntent.putExtra("email",email);
        photospec.setContent(photosIntent);
        
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Notification");        
        songspec.setIndicator("Notification", getResources().getDrawable(R.drawable.ic_launcher));
        Intent songsIntent = new Intent(this, DashboardActivity.class);
        songsIntent.putExtra("email",email);
        songspec.setContent(songsIntent);
        
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("Profile");
        videospec.setIndicator("Profile", getResources().getDrawable(R.drawable.ic_launcher));
        Intent videosIntent = new Intent(this, DashboardActivity.class);
        videosIntent.putExtra("email",email);
        videospec.setContent(videosIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab
    }
}