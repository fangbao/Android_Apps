package edu.stevens.lrc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcProcessor {

	public List<Queue> process(InputStream inputStream){
	
		Queue<Long> timeMills=new LinkedList<Long>(); //store the time stamps
		Queue<String> messages=new LinkedList<String>();//store the lrc information
		
		List<Queue> queues=new ArrayList<Queue>(); //store this two queues
		
		try{
			
			InputStreamReader is=new InputStreamReader(inputStream,"GB2312");
			BufferedReader br=new BufferedReader(is);
			String temp=null;
			int i=0;
			
			Pattern p=Pattern.compile("\\[([^\\]]+)\\]");
			String result=null;
			boolean b=true;
			while((temp=br.readLine())!=null){
				i++;
				Matcher matcher=p.matcher(temp);
				if(matcher.find()){
					
					if(result!=null){
						messages.add(result);
					}
					
					String timeStr=matcher.group();
					Long timeMill=time2Long(timeStr.substring(1, timeStr.length()-1));
					if(b){
						timeMills.offer(timeMill);
					}
					String msg=temp.substring(10);
					result=msg+"\n";
					
				}else{
					result=result+temp+"\n";
				}
			}
			messages.add(result);
			
			queues.add(timeMills);
			queues.add(messages);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return queues;
	}

	private Long time2Long(String timeStr) {
		String strArray[]=timeStr.split(":");
		int min=Integer.parseInt(strArray[0]);
		String strArray2[]=strArray[1].split("\\.");
		int sec=Integer.parseInt(strArray2[0]);
		int mill=Integer.parseInt(strArray2[1]);
		return min*60*1000+sec*1000+mill*10L;
	}
	
}
