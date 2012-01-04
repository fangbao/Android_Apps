package edu.stevens.mp3player;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//get tabHost object. It will finish all of step for tabs
		TabHost tabHost=getTabHost();
		Intent remoteIntent=new Intent();
		remoteIntent.setClass(this, MP3ListActivity.class);  //refer to an activity
		
		//create tabspec object that indicate a tab
		TabHost.TabSpec remoteSpec=tabHost.newTabSpec("Remote");
		Resources res=getResources();
		//set the indicator
		remoteSpec.setIndicator("Remote",res.getDrawable(android.R.drawable.stat_sys_download));
		remoteSpec.setContent(remoteIntent);
		tabHost.addTab(remoteSpec); //add the tab to tabHost
		
		Intent localIntent=new Intent();
		localIntent.setClass(this, LocalMP3ListActivity.class);
		TabHost.TabSpec localSpec=tabHost.newTabSpec("Local");
		localSpec.setIndicator("Local",res.getDrawable(android.R.drawable.stat_sys_upload));
		localSpec.setContent(localIntent);
		tabHost.addTab(localSpec);
	}
}
