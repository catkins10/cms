package com.yuanluesoft.jeaf.tools.deployment;

import java.io.File;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 设置文件时间
 * @author linchuan
 *
 */
public class ResetFileTime {

	public static void main(String[] args) {
		String[] folders = {"C:\\Workspace\\cms\\WebContent\\jeaf\\dialog\\images\\"};
		/*{"D:\\workspace\\cms\\JavaSource\\com\\yuanluesoft\\jeaf\\fingerprint",
							"D:\\workspace\\cms\\WebContent\\jeaf\\fingerprint",
							"D:\\workspace\\cms\\WebContent\\WEB-INF\\jeaf\\fingerprint"};*/
		ResetFileTime resetFileTime = new ResetFileTime();
		Timestamp newTime = DateTimeUtils.now();
		//newTime = DateTimeUtils.set(newTime, java.util.Calendar.YEAR, 2009);
		for(int i=0; i<folders.length; i++) {
			//resetFileTime.setFileTime(new File(folders[i]), "hibernate-config.xml", newTime.getTime());
			resetFileTime.setFileTime(new File(folders[i]), null, newTime.getTime());
		}
	}
	
	private void setFileTime(File folder, String fileName, long newTime) {
		if(folder.isFile()) {
			folder.setLastModified(newTime);
			System.out.println(folder.getPath() + "\t" + new Timestamp(folder.lastModified()) + "\t" + (newTime==folder.lastModified()));
			return;
		}
		File[] files = folder.listFiles();
		if(files==null || files.length==0) {
			return;
		}
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) {
				setFileTime(files[i], fileName, newTime);
			}
			else if(fileName==null || files[i].getName().equals(fileName)) {
				files[i].setLastModified(newTime);
				System.out.println(files[i].getPath() + "\t" + new Timestamp(files[i].lastModified()) + "\t" + (newTime==files[i].lastModified()));
			}
		}
	}
}
