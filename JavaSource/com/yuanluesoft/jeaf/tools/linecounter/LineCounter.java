package com.yuanluesoft.jeaf.tools.linecounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 行计数器
 * @author linchuan
 *
 */
public class LineCounter {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LineCounter lineCounter = new LineCounter();
		try {
			int count = lineCounter.count(new File("D:/workspace/cms/JavaSource/com/yuanluesoft/cms/publicservice/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/cms/publicservice/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/WEB-INF/cms/publicservice/"), 0);
			
			
			count += lineCounter.count(new File("D:/workspace/cms/JavaSource/com/yuanluesoft/cms/leadermail/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/cms/leadermail/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/WEB-INF/cms/leadermail/"), 0);
			
			
			count += lineCounter.count(new File("D:/workspace/cms/JavaSource/com/yuanluesoft/cms/complaint/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/cms/complaint/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/WEB-INF/cms/complaint/"), 0);
			
			count += lineCounter.count(new File("D:/workspace/cms/JavaSource/com/yuanluesoft/cms/messageboard/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/cms/messageboard/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/WEB-INF/cms/messageboard/"), 0);
			
			count += lineCounter.count(new File("D:/workspace/cms/JavaSource/com/yuanluesoft/cms/interview/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/cms/interview/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/WEB-INF/cms/interview/"), 0);
			
			count += lineCounter.count(new File("D:/workspace/cms/JavaSource/com/yuanluesoft/cms/supervision/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/cms/supervision/"), 0);
			count += lineCounter.count(new File("D:/workspace/cms/WebContent/WEB-INF/cms/supervision/"), 0);
			System.out.println("******count:" + count);
		}
		catch (Exception e) {
			
		}
	}
	
	/**
	 * 转换整个目录
	 * @param directory
	 */
	private int count(File directory, int count) {
		//查找js文件
		File[] sourceFiles = directory.listFiles(new FileFilter() {
	        public boolean accept(File file) {
	            return file.getName().endsWith(".java") || file.getName().endsWith(".jsp") || file.getName().endsWith(".xml") || file.getName().endsWith(".js");
	        }
	    } );
		if(sourceFiles!=null) { //找到源码文件
			//计算源码行数
			for(int i=0; i<sourceFiles.length; i++) {
				try {
					count += countLine(sourceFiles[i]);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//查找下级目录
		File[] subDirectories = directory.listFiles( new FileFilter() {
	        public boolean accept(File file) {
	            return file.isDirectory();
	        }
		});
		if(subDirectories!=null && subDirectories.length>0) {
			for(int i = 0; i<subDirectories.length; i++) {
				//处理下一级目录
				count = count(subDirectories[i], count);
			}
		}
		return count;
	}
	
	private int countLine(File sourceFile) throws Exception {
		FileInputStream input = new FileInputStream(sourceFile);
		InputStreamReader inputStreamReader = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		int i = 0;
		for(; reader.readLine()!=null; i++);
		reader.close();
		inputStreamReader.close();
		input.close();
		System.out.println(sourceFile.getPath() + ": " + i);
		return i;
	}
}
