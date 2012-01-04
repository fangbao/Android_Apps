package edu.stevens.mp3player;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Queue;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.stevens.lrc.LrcProcessor;
import edu.stevens.model.MP3Info;
import edu.stevens.mp3player.service.PlayerService;

public class PlayerActivity extends Activity {
	private ImageButton beginBtn;
	private ImageButton pauseBtn;
	private ImageButton stopBtn;
	private TextView lrcTextView;
	private BroadcastReceiver receiver;
	private IntentFilter intentFilter;
	private MP3Info info;
	
//	private List<Queue> queues;
//	private UpdateTimeCallBack updateTimeCallBack;
//	private long begin=0;
//	private long nextTimeMill=0;
//	private long currentTimeMill=0;
//	private String message;
//	private long pauseTimeMills=0;
//	private Handler handler=new Handler();
			
	
//	private boolean isReleased=true;
//	private boolean isPaused=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		Intent intent = getIntent();
		info = (MP3Info) intent.getSerializableExtra("mp3info");    //get data from previous activity

		beginBtn = (ImageButton) findViewById(R.id.begin);
		pauseBtn = (ImageButton) findViewById(R.id.pause);
		stopBtn = (ImageButton) findViewById(R.id.stop);
		lrcTextView=(TextView)findViewById(R.id.lrcTextView);

		beginBtn.setOnClickListener(beginListener);
		pauseBtn.setOnClickListener(pauseListener);
		stopBtn.setOnClickListener(stopListener);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		receiver=new LrcMessageBroadcastReceiver();   
		registerReceiver(receiver, getIntentFilter());		//register a receiver for lrc broadcast 
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);    //when activity is invisible, unregister lrc receiver.
	}
	
	
	private OnClickListener beginListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.putExtra("mp3info", info);		//transfer "play" message to the player service
			intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
			intent.setClass(PlayerActivity.this, PlayerService.class);
//			if(isReleased){
//				isReleased=false;
//				prepareLrc(info.getLrcName());
//				begin=System.currentTimeMillis();
//			}else if(isPaused){
//				begin=System.currentTimeMillis()-pauseTimeMills+begin;
//			}
//			isPaused=false;
			startService(intent);
//			handler.postDelayed(updateTimeCallBack,5);
		}
	};

	private OnClickListener pauseListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent=new Intent();  //transfer "pause" message to the player service
			intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
			intent.setClass(PlayerActivity.this, PlayerService.class);
			startService(intent);
			
//			handler.removeCallbacks(updateTimeCallBack);
//			if(!isPaused)
//				pauseTimeMills=System.currentTimeMillis();
//			
//			isPaused=true;
		}
	};

	private OnClickListener stopListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent=new Intent();  //transfer "stop" message to the player service
			intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
			intent.setClass(PlayerActivity.this, PlayerService.class);
			startService(intent);

//			handler.removeCallbacks(updateTimeCallBack);
//			isReleased=true;
		}
	};
	
//	private void prepareLrc(String lrcName){
//		try {
//			InputStream inputStream=new FileInputStream(getLrcPath(lrcName));
//			LrcProcessor processor=new LrcProcessor();
//			queues=processor.process(inputStream);
//			updateTimeCallBack=new UpdateTimeCallBack(queues);
//			begin=0;
//			nextTimeMill=0;
//			currentTimeMill=0;
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
	
//	private String getLrcPath(String lrcName) {
//		String SDCardRoot = Environment.getExternalStorageDirectory()
//				.getAbsolutePath();
//		String path = SDCardRoot + File.separator + "mp3" + File.separator
//				+ lrcName;
//		return path;
//	}
	
	private IntentFilter getIntentFilter() {	//create a specific intent filter
		if(intentFilter==null){
			intentFilter=new IntentFilter();
			intentFilter.addAction(AppConstant.LRC_MESSAGE_ACTION);
		}
		return intentFilter;
	}
	
	public class LrcMessageBroadcastReceiver extends BroadcastReceiver{

		/**
		 * call back method
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			String lrcMessage=intent.getStringExtra("lrcMessage");
			lrcTextView.setText(lrcMessage);  //after receive the message, update the text view.
		}
		
	}
	
	
	
//	public class UpdateTimeCallBack implements Runnable{
//
//		
//		private Queue times;
//		private Queue messages;
//		
//		
//		public UpdateTimeCallBack(List<Queue> queues) {
//			times=queues.get(0);
//			messages=queues.get(1);
//		}
//
//		@Override
//		public void run() {
//			long offset=System.currentTimeMillis()-begin;
//			if(currentTimeMill==0){
//				nextTimeMill=(Long)times.poll();
//				message=(String)messages.poll();
//				lrcTextView.setText(message);
//			}
//			if(offset>=nextTimeMill){
//				lrcTextView.setText(message);
//				nextTimeMill=(Long)times.poll();
//				message=(String)messages.poll();
//			}
//			
//			currentTimeMill=currentTimeMill+10;
//			
//			handler.postDelayed(updateTimeCallBack, 10);
//		}
//		
//	}
	
}
