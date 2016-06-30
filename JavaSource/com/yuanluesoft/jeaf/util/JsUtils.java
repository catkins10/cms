package com.yuanluesoft.jeaf.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author linchuan
 *
 */
public class JsUtils {

	/**
	 * 调整并输出JS文件
	 * @param jsFile
	 * @param writer
	 * @throws IOException
	 */
	public static void rewriteJsFile(String jsFile, BufferedWriter writer) throws IOException {
		FileInputStream input = new FileInputStream(jsFile);
		InputStreamReader inputStreamReader = new InputStreamReader(input, "utf-8");
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String line;
		boolean remark = false;
		int index;
		while((line=reader.readLine())!=null) {
			//删除所有注释
			if(remark) {
				//查找*/
				index = line.indexOf("*/");
				if(index==-1) { //注释未结束,不输出该行
					continue;
				}
				remark = false;
				if(index + 2==line.length()) {
					continue;
				}
				line = line.substring(index + 2);
			}
			for(index = line.indexOf("//"); index!=-1; index = line.indexOf("//", index)) {
				//后面有引号的,不是注释
				int indexQuot;
				if((indexQuot=line.indexOf('\"', index + 2))!=-1) {
					index = indexQuot + 1;
				}
				else if((indexQuot=line.indexOf('\'', index + 2))!=-1) {
					index = indexQuot + 1;
				}
				else {
					line = line.substring(0, index);
					break;
				}
			}
			if("".equals(line)) {
				continue;
			}
			//查找/*
			for(index = line.indexOf("/*"); index!=-1; index = line.indexOf("/*", index)) {
				if(line.substring(index + 2).startsWith("@")) {
					index += 2;
					continue;
				}
				//后面有引号的,不是注释
				int indexEnd = line.indexOf("*/", index + 2); //查找*/
				if(indexEnd==-1) {
					remark = true;
					line = line.substring(0, index);
					break;
				}
				else {
					line = line.substring(0, index) + line.substring(indexEnd + 2);
				}
			}
			//把中文转换成utf8编码形式,写入新文件
			try {
				writer.write(Encoder.getInstance().utf8JsEncode((line)));
			}
			catch (Exception e) {
			
			}
			writer.write('\n');
		}
		reader.close();
		inputStreamReader.close();
		input.close();
	}
	
	/**
	 * 合并JS文件
	 * @param srcFiles
	 * @param srcFolders
	 * @param destFile
	 * @throws Exception
	 */
	public static void combine(final String[] srcFiles, String[] srcFolders, String destFile) throws Exception {
		FileWriter output = new FileWriter(destFile);
		BufferedWriter writer = new BufferedWriter(output);
		for(int i=0; i<(srcFiles==null ? 0 : srcFiles.length); i++) {
			System.out.println(srcFiles[i]);
			JsUtils.rewriteJsFile(srcFiles[i], writer);
			writer.write("\r\n");
		}
		for(int i=0; i<srcFolders.length; i++) {
			File[] files = new File(srcFolders[i]).listFiles(new FileFilter() {
				public boolean accept(File file) {
					if(!file.getName().endsWith(".js")) { //不是JS
						return false;
					}
					//检查是否已经在srcFiles中出现过
					String fileName = file.getPath().replace('\\', '/');
					for(int j=0; j<(srcFiles==null ? 0 : srcFiles.length); j++) {
						if(fileName.equalsIgnoreCase(srcFiles[j].replace('\\', '/'))) {
							return false;
						}
					}
					return true;
				}
			});
			for(int j=0; j<files.length; j++) {
				System.out.println(files[j].getPath());
				JsUtils.rewriteJsFile(files[j].getPath(), writer);
				writer.write("\r\n");
			}
		}
		writer.close();
		output.close();
	}
}