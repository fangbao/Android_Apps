package edu.stevens.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import edu.stevens.model.MP3Info;

public class FileUtils {
	private String SDCardRoot;

	public String getSDPATH() {
		return SDCardRoot;
	}

	public FileUtils() {
		// 得到当前外部存储设备的目录
		// /SDCARD
		SDCardRoot = Environment.getExternalStorageDirectory() + File.separator;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatFileInSDCard(String fileName, String dir)
			throws IOException {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDCardRoot + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatFileInSDCard(fileName, path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = input.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * read the name and size of mp3 in the content
	 */
	public List<MP3Info> getMP3Files(String path) {
		List<MP3Info> list = new ArrayList<MP3Info>();
		File file = new File(SDCardRoot + File.separator + path);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.getName().endsWith("mp3")) {
				MP3Info info = new MP3Info();
				info.setMp3Name(f.getName());
				info.setMp3Size(f.length() + "");
				
				
				for(File f2:files){
					if(f2.getName().startsWith(info.getMp3Name().substring(0,info.getMp3Name().length()-3))&&f2.getName().endsWith("lrc")){
						info.setLrcName(f2.getName());
						info.setLrcSize(f2.length()+"");
					}
				}
				
				list.add(info);
			}
		}
		return list;
	}

}