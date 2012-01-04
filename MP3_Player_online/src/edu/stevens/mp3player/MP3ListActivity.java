package edu.stevens.mp3player;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import edu.stevens.download.HttpDownloader;
import edu.stevens.model.MP3Info;
import edu.stevens.mp3player.service.DownloadService;
import edu.stevnes.xml.MP3ListContentHandler;

public class MP3ListActivity extends ListActivity {

	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	private List<MP3Info> mp3Infos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_mp3_list);
		updateListView();
	}

	/**
	 * after user clicking the menu, the method will be called
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, UPDATE, 1, R.string.mp3list_update);
		menu.add(0, ABOUT, 2, R.string.mp3list_about);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (UPDATE == item.getItemId()) {
			updateListView();
		} else if (ABOUT == item.getItemId()) {// user clicked the 'about' item

		}

		return super.onOptionsItemSelected(item);
	}
	
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//according to the position the user clicked, we can get the the corresponding mps info.
		MP3Info info=mp3Infos.get(position);
		
		Intent intent=new Intent();
		//put mp3 info object into the intent object.
		intent.putExtra("mp3info", info);
		intent.setClass(this, DownloadService.class);
		startService(intent);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	private String downloadXML(String urlStr) {
		HttpDownloader downloader = new HttpDownloader();
		String result = downloader.download(urlStr);
		return result;
	}

	private List<MP3Info> parse(String xmlStr) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		List<MP3Info> infos = null;
		try {
			XMLReader reader = factory.newSAXParser().getXMLReader();

			infos = new ArrayList<MP3Info>();
			MP3ListContentHandler handler = new MP3ListContentHandler(infos);
			reader.setContentHandler(handler);
			reader.parse(new InputSource(new StringReader(xmlStr)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return infos;
	}

	private SimpleAdapter buildSimpleAdapter(List<MP3Info> infos) {
		// generate a list object and put mp3 info into the list based on
		// SimpleAdapter rule.
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (MP3Info info : infos) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", info.getMp3Name());
			map.put("mp3_size", info.getMp3Size());
			list.add(map);
		}
		// create a simpleAdapter object
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" },
				new int[] { R.id.mp3_name, R.id.mp3_size });
		return adapter;
	}

	private void updateListView() {
		// user clicked the 'update' item
		// download the basic informations of mp3s
		String xml = downloadXML(AppConstant.URL.BASE_URL+"resources.xml");
		// put mp3 object into the mp3 list
		mp3Infos = parse(xml);

		// set this adapter to the list activity
		setListAdapter(buildSimpleAdapter(mp3Infos));
	}

}