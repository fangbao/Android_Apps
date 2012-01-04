package edu.stevens.mp3player.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Queue;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import edu.stevens.lrc.LrcProcessor;
import edu.stevens.model.MP3Info;
import edu.stevens.mp3player.AppConstant;

public class PlayerService extends Service {

	private MediaPlayer player;

	private boolean isPausing = false;
	private boolean isReleased = false;

	private List<Queue> queues;
	private UpdateTimeCallBack updateTimeCallBack;
	private long begin = 0;
	private long nextTimeMill = 0;
	private long currentTimeMill = 0;
	private String message;
	private long pauseTimeMills = 0;
	private Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		MP3Info info = (MP3Info) intent.getSerializableExtra("mp3info");
		int MSG = intent.getIntExtra("MSG", 0);
		if (info != null) {
			if (MSG == AppConstant.PlayerMsg.PLAY_MSG) {
				play(info);		//based on the type of msg, run different method.
			}
		} else {
			if (MSG == AppConstant.PlayerMsg.PAUSE_MSG) {
				pause();
			} else if (MSG == AppConstant.PlayerMsg.STOP_MSG) {
				stop();
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void stop() {
		if (player != null) {
			if (!isReleased) {
				player.stop();
				player.release();
				isReleased = true;
				handler.removeCallbacks(updateTimeCallBack);
			}
			player = null;
		}

	}

	private void pause() {
		if (player != null && isReleased == false) {
			handler.removeCallbacks(updateTimeCallBack);
			if (!isPausing) {
				player.pause();
				isPausing = true;
				
				pauseTimeMills=System.currentTimeMillis();
			}
		}

	}

	private void play(MP3Info info) {
		if (player == null) {
			String path = getMP3Path(info);
			player = MediaPlayer.create(this, Uri.parse("file://" + path));
			player.setLooping(false);
			player.setOnCompletionListener(new OnCompletionListener() {				
				@Override				//call back method when songs finished
				public void onCompletion(MediaPlayer mp) {
					player.release();
					isReleased = true;
					handler.removeCallbacks(updateTimeCallBack);
					player=null;
				}
			});
			prepareLrc(info.getLrcName());     //initial the lrc enviroment
			begin=System.currentTimeMillis();
		}
		if(isPausing)
			begin=System.currentTimeMillis()-pauseTimeMills+begin;
		player.start();
		isReleased = false;
		isPausing = false;
		
		handler.postDelayed(updateTimeCallBack,5);  //post the callback into message queue
	}

	private void prepareLrc(String lrcName) {
		try {
			InputStream inputStream = new FileInputStream(getLrcPath(lrcName));
			LrcProcessor processor = new LrcProcessor();
			queues = processor.process(inputStream);
			updateTimeCallBack = new UpdateTimeCallBack(queues);  //create callback task
			begin = 0;
			nextTimeMill = 0;
			currentTimeMill = 0;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String getMP3Path(MP3Info info) {
		String SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String path = SDCardRoot + File.separator + "mp3" + File.separator
				+ info.getMp3Name();
		return path;
	}

	private String getLrcPath(String lrcName) {
		String SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String path = SDCardRoot + File.separator + "mp3" + File.separator
				+ lrcName;
		return path;
	}

	public class UpdateTimeCallBack implements Runnable {

		//two synchronized queues for showing the lyrics information 
		private Queue times;
		private Queue messages;

		public UpdateTimeCallBack(List<Queue> queues) {
			times = queues.get(0);
			messages = queues.get(1);
		}

		@Override
		public void run() {
			long offset = System.currentTimeMillis() - begin;  //the time offset for playing music
			if (currentTimeMill == 0) {
				nextTimeMill = (Long) times.poll();
				message = (String) messages.poll();

			}
			if (offset >= nextTimeMill) {
				Intent intent = new Intent();
				intent.putExtra("lrcMessage", message);
				intent.setAction(AppConstant.LRC_MESSAGE_ACTION);
				sendBroadcast(intent);

				nextTimeMill = (Long) times.poll();
				message = (String) messages.poll();
			}

			currentTimeMill = currentTimeMill + 10;

			handler.postDelayed(updateTimeCallBack, 10); //every 10 ms, put the call back task into queue
		}

	}

}
