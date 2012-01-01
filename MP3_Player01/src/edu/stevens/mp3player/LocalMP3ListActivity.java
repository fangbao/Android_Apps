package edu.stevens.mp3player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import edu.stevens.model.MP3Info;
import edu.stevens.utils.FileUtils;

public class LocalMP3ListActivity extends ListActivity {

	private List<MP3Info> infos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_mp3_list);
	}

	@Override
	protected void onResume() {
		
		FileUtils utils = new FileUtils();
		infos = utils.getMP3Files("mp3/");
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (MP3Info info : infos) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", info.getMp3Name());
			map.put("mp3_size", info.getMp3Size());
			list.add(map);
		}

		 SimpleAdapter adapter=new
		 SimpleAdapter(this,list,R.layout.mp3info_item,new
		 String[]{"mp3_name","mp3_size"},new
		 int[]{R.id.mp3_name,R.id.mp3_size});

		setListAdapter(adapter);
		super.onResume();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(infos!=null){
			MP3Info info=infos.get(position);
			Intent intent=new Intent();
			intent.putExtra("mp3info", info);
			intent.setClass(this, PlayerActivity.class);
			startActivity(intent);
		}
		
	}
	
	
}
