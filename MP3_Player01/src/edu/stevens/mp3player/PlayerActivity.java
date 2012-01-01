package edu.stevens.mp3player;

import java.io.File;

import edu.stevens.model.MP3Info;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class PlayerActivity extends Activity {
	private ImageButton beginBtn;
	private ImageButton pauseBtn;
	private ImageButton stopBtn;
	private MediaPlayer player;

	private boolean isPausing=false;
	private boolean isReleased=false;
	
	private MP3Info info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		Intent intent = getIntent();
		info = (MP3Info) intent.getSerializableExtra("mp3info");

		beginBtn = (ImageButton) findViewById(R.id.begin);
		pauseBtn = (ImageButton) findViewById(R.id.pause);
		stopBtn = (ImageButton) findViewById(R.id.stop);

		beginBtn.setOnClickListener(beginListener);
		pauseBtn.setOnClickListener(pauseListener);
		stopBtn.setOnClickListener(stopListener);
		
	}

	private OnClickListener beginListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(player==null){
				String path=getMP3Path(info);
				player=MediaPlayer.create(PlayerActivity.this,Uri.parse("file://"+path));
				player.setLooping(false);
			}
			player.start();
			isReleased=false;
			isPausing=false;
		}
	};

	private OnClickListener pauseListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(player!=null&&isReleased==false){
				if(!isPausing){
					player.pause();
					isPausing=true;
				}
			}

		}
	};

	private OnClickListener stopListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(player!=null){
				if(!isReleased){
					player.stop();
					player.release();
					isReleased=true;
				}
				player=null;
			}

		}
	};
	
	private String getMP3Path(MP3Info info){
		String SDCardRoot=Environment.getExternalStorageDirectory().getAbsolutePath();
		String path=SDCardRoot+File.separator+"mp3"+File.separator+info.getMp3Name();
		return path;
	}
}
