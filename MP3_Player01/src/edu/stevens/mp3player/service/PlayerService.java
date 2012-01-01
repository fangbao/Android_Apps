package edu.stevens.mp3player.service;

import edu.stevens.model.MP3Info;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlayerService extends Service {

	private MediaPlayer player;

	private boolean isPausing = false;
	private boolean isReleased = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		MP3Info info=(MP3Info)intent.getSerializableExtra("mp3info");
		int MSG=intent.getIntExtra("MSG", 0);
		if(info!=null){
			//------------------------------------------
		}
		
		
		return super.onStartCommand(intent, flags, startId);
	}

}
