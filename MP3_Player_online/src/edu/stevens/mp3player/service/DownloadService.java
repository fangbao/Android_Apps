package edu.stevens.mp3player.service;

import edu.stevens.download.HttpDownloader;
import edu.stevens.model.MP3Info;
import edu.stevens.mp3player.AppConstant;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	//When user click the item in the list, the method will be called
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//get info from the intent
		MP3Info info=(MP3Info)intent.getSerializableExtra("mp3info");
		
		//create a thread and pass the info object into the thread
		Thread thread=new Thread(new DownloadThread(info));
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	class DownloadThread implements Runnable{

		private MP3Info info;
		
		public DownloadThread(MP3Info info){
			this.info=info;
		}
		
		@Override
		public void run() {
			//download URL: http://192.168.1.104:8082/mp3/a1.mp3
			//based on the name of mp3, it will generare the URL
			String urlStr=AppConstant.URL.BASE_URL+info.getMp3Name();
			
			//lrc URL
			String urlStr2=AppConstant.URL.BASE_URL+info.getLrcName();
			HttpDownloader downloader=new HttpDownloader();
			//download the file and save it into SDCard
			int result=downloader.downFile(urlStr, "mp3/", info.getMp3Name());
			downloader.downFile(urlStr2, "mp3/", info.getLrcName());
			String resultMsg=null;
			if(result==-1){
				resultMsg="download failed.";
			}else if(result==1){
				resultMsg="File exsited. Do not need to download again.";
			}else if(result==0){
				resultMsg="download successed";
			}
			//Using the notification to inform the result to users.
			
			//--------------------------------------------------------------------
		}
		
	}

	
}
