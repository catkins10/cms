/*
 * Created on 2006-9-14
 *
 */
package com.yuanluesoft.jeaf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;

/**
 * 
 * @author linchuan
 * 
 */
public class FileUtils {
	
	/**
	 * 文件是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean isExists(String fileName) {
		return new File(fileName).exists();
	}
	
	/**
	 * 是否空目录
	 * @param dir
	 * @return
	 */
	public static boolean isEmpty(String dir) {
		File[] files = new File(dir).listFiles();
		return files==null || files.length==0;
	}
	
	/**
	 * 文件名称编码
	 * @param fileName
	 * @param enc
	 * @return
	 */
	public static String encodeFileName(String fileName, String enc) {
		try {
			return URLEncoder.encode(fileName.replaceAll("\\x23", "%23"), "utf-8").replaceAll("\\x2b", "%20");
		}
		catch (UnsupportedEncodingException e) {
			return fileName;
		}
	}
	
	/**
	 * 拷贝文件
	 * @param src
	 * @param dest
	 * @param overwrite
	 * @param autoRename
	 * @return
	 */
	public static String copyFile(String src, String dest, boolean overwrite, boolean autoRename) {
		File destFile = new File(dest);
		if(destFile.isDirectory()) { //目录
			if(!dest.endsWith("/")) {
				dest += "/";
			}
			int index = src.lastIndexOf('/');
			if(index==-1) {
				index = src.lastIndexOf('\\');
			}
			dest += src.substring(index + 1);
			destFile = new File(dest);
		}
		File srcFile = new File(src);
		if(destFile.exists()) { //文件已存在,且不允许覆盖
			if(srcFile.lastModified()==destFile.lastModified()) { //文件最后修改时间相同,不需要拷贝
				return dest;
			}
			if(!overwrite) {
				if(!autoRename) {
					return null;
				}
				dest = autoRename(dest);
			}
		}
		byte[] buffer = new byte[Math.max(1, Math.min((int)srcFile.length(), 81920))];
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			int readLen;
			while((readLen=in.read(buffer))>0) {
				out.write(buffer, 0, readLen);
			}
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
		finally {
			try {
				in.close();
			}
			catch(Exception e) {
				
			}
			try {
				out.close();
			}
			catch(Exception e) {
				
			}
		}
		destFile.setLastModified(srcFile.lastModified());
		return dest;
	}
	
	/**
	 * 自动更名
	 * @param fileName
	 * @return
	 */
	private static String autoRename(String fileName) {
		File file = new File(fileName);
		int index = fileName.lastIndexOf('.');
		String prefix;
		String postfix;
		if(index!=-1) {
			prefix = fileName.substring(0, index);
			postfix = fileName.substring(index);
		}
		else {
			prefix = fileName;
			postfix = "";
		}
		for(int i=1; file.exists(); i++) {
			file = new File(prefix + "(" + i + ")" + postfix);
		}
		return file.getPath();
	}
	
	/**
	 * 
	 * @param filePath
	 * @param newFileName
	 * @return
	 */
	public static String renameFile(String filePath, String newFileName) {
		filePath = filePath.replace('\\', '/');
		int index = filePath.lastIndexOf('/');
		String newFilePath = filePath.substring(0, index + 1) + FileUtils.retrieveFileName(newFileName);
		return FileUtils.renameFile(filePath, newFilePath, true, true);
	}
	
	/**
	 * 文件更名
	 * @param src
	 * @param dest
	 * @param replace
	 * @return
	 */
	public static String renameFile(String src, String dest, boolean replace, boolean autoRename) {
		if(src.replace('\\', '/').equalsIgnoreCase(dest.replace('\\', '/'))) {
			return src;
		}
		File srcFile = new File(src);
		if(!srcFile.exists()) {
			return null;
		}
		File file = new File(dest);
		if(file.exists()) {
			if(replace) {
				deleteFile(dest, true);
			}
			else if(autoRename) {
				dest = autoRename(dest);
				file = new File(dest);
			}
		}
		for(int i=0; i<10; i++) {
			if(srcFile.renameTo(file)) {
				return file.getPath();
			}
			try {
				Thread.sleep(50);
			}
			catch (InterruptedException e) {
				
			}
		}
		return null;
	}
	
	/**
	 * 目录更名
	 * @param src
	 * @param dest
	 * @param replace
	 * @param autoRename
	 * @return
	 */
	public static String renameDirectory(String src, String dest, boolean replace, boolean autoRename) {
		checkDirectoryDeleteable(src);
		File srcDir = new File(src);
		if(!srcDir.exists()) {
			return null;
		}
		File dir = new File(dest);
		if(dir.exists()) {
			if(replace) {
				deleteDirectory(dest);
			}
			else if(autoRename) {
				dest = autoRename(dest);
				dir = new File(dest);
			}
		}
		return (srcDir.renameTo(dir) ? dir.getPath() : null);
	}
	
	/**
	 * 移动文件
	 * @param src
	 * @param dest
	 * @param replace
	 * @return
	 */
	public static String moveFile(String src, String destDir, boolean replace, boolean autoRename) {
		String name = src.substring(src.lastIndexOf('/') + 1);
		if(!destDir.endsWith("/")) {
			destDir += "/";
		}
		return renameFile(src, destDir + name, replace, autoRename);
	}
	
	/**
	 * 删除文件
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		return deleteFile(filePath, true);
	}
	
	/**
	 * 删除文件
	 * @param filePath
	 * @param warn
	 * @return
	 */
	private static boolean deleteFile(String filePath, boolean warn) {
		if(warn) {
			Logger.warn("Delete file " + filePath + ".");
		}
		File curFile = new File(filePath);
		int i = 0;
		for(; i<20 && !curFile.delete(); i++) {
			try {
				System.gc();
				Thread.sleep(50);
			}
			catch (InterruptedException e) {
				
			}
		}
		return i<20;
	}
	
	/**
	 * 删除多个文件
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFiles(List filePaths) {
		if(filePaths==null) {
			return true;
		}
		for(Iterator iterator = filePaths.iterator(); iterator.hasNext();) {
			String filePath = (String)iterator.next();
			if(!deleteFile(filePath, true)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 拷贝目录
	 * @param src
	 * @param dest
	 * @param containChildDirectories
	 * @param overwrite
	 */
	public static void copyDirectory(String src, String dest, boolean containChildDirectories, boolean overwrite) {
		if(!src.endsWith("/")) {
			src += "/";
		}
		if(!dest.endsWith("/")) {
			dest += "/";
		}
		File srcDir = new File(src);
		File[] files = srcDir.listFiles();
		for(int i=0; i<files.length; i++) {
			if(files[i].isFile()) { //文件
				copyFile(src + files[i].getName(), dest + files[i].getName(), overwrite, false);
			}
			else if(files[i].isDirectory() && containChildDirectories) { //目录
				File destDir = new File(dest + files[i].getName());
				if(!destDir.exists()) {
					destDir.mkdir();
				}
				copyDirectory(src + files[i].getName(), dest + files[i].getName(), containChildDirectories, overwrite);
			}
		}
	}
	
	/**
	 * 删除目录中的文件
	 * @param dir
	 * @return
	 */
	public static boolean deleteFilesInDirectory(String dir) {
		checkDirectoryDeleteable(dir);
		Logger.warn("Delete files in directory " + dir + ".");
		return deleteDirectory(new File(dir), 0, false);
	}
	
	/**
	 * 删除修改时间在fileTime前的文件
	 * @param dir
	 * @param fileTime
	 * @return
	 */
	public static boolean deleteFilesInDirectory(String dir, Timestamp fileTime) {
		checkDirectoryDeleteable(dir);
		Logger.warn("Delete files in directory " + dir + " that modify time before " + DateTimeUtils.formatTimestamp(fileTime, null) + ".");
		return deleteDirectory(new File(dir), fileTime.getTime(), false);
	}
	
	/**
	 * 当目录为空时删除目录
	 * @param dir
	 * @return
	 */
	public static boolean deleteDirectoryIfEmpty(String dir) {
		checkDirectoryDeleteable(dir);
		boolean result = new File(dir).delete();
		if(result) {
			Logger.warn("Delete directory " + dir + " when directory is empty.");
		}
		return result;
	}
	
	/**
	 * 删除目录
	 * @param dir
	 * @return
	 */
	public static boolean deleteDirectory(String dir) {
		checkDirectoryDeleteable(dir);
		if(!isEmpty(dir)) {
			Logger.warn("Delete directory " + dir + ".");
		}
		return deleteDirectory(new File(dir), 0, true);
	}
	
	/**
	 * 删除目录,递归函数
	 * @param dir
	 * @return
	 */
	private static boolean deleteDirectory(File dir, long fileTime, boolean deleteSelf) {
		if(dir.exists()) {
			File[] files = dir.listFiles();
			for(int i=files.length - 1; i>=0; i--) {
				if(fileTime>0 && files[i].lastModified()>=fileTime) { //有指定删除时间,且文件修改时间在删除时间后
					continue; //不删除
				}
				if(files[i].isDirectory()) {
					deleteDirectory(files[i], fileTime, true);
				}
				else {
					deleteFile(files[i].getPath(), false);
				}
			}
			if(deleteSelf) {
				dir.delete();
			}
		}
		return true;
	}
	
	/**
	 * 检查目录是否被允许删除
	 * @param dir
	 * @return
	 */
	private static void checkDirectoryDeleteable(String dir) {
		String path = new File(dir).getPath();
		//检查是否程序目录
		String[] undeleteablePaths = {Environment.getWebAppPath(), Environment.getWebinfPath(), "/"};
		for(int i=0; i<undeleteablePaths.length; i++) {
			if(new File(undeleteablePaths[i]).getPath().equals(path)) {
				throw new Error("delete disable");
			}
		}
		//检查是否windows磁盘
		for(char i='a'; i<='z'; i++) {
			if(new File(i + ":/").getPath().equals(path)) {
				throw new Error("delete disable");
			}
		}
		//检查是否模板、页面等目录
		undeleteablePaths = new String[]{"tempatePath", "sitePagePath", "attachmentDirectory"}; //, "staticPagePath"
		for(int i=0; i<undeleteablePaths.length; i++) {
			try {
				if(new File((String)Environment.getService(undeleteablePaths[i])).getPath().equals(path)) {
					throw new Error("delete disable");
				}
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 创建目录,返回以“/”结束的路径名
	 * @param dir
	 * @return
	 */
	public static String createDirectory(String dir) {
		dir = dir.replaceAll("\\\\", "/");
		if(!dir.endsWith("/")) {
			dir += "/";
		}
		File file = new File(dir);
		if(!file.exists()) {
			file.mkdirs();
		}
		return dir;
	}
	
	/**
	 * 替换文件中的字符串
	 * @param filePath
	 * @param from
	 * @param to
	 * @throws Exception
	 */
	public static void replaceStringInFile(String filePath, String from, String to) throws Exception {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		try {
			fileReader = new FileReader(filePath);
			bufferedReader = new BufferedReader(fileReader);
			fileWriter = new FileWriter(filePath + ".temp");
			for(String line = bufferedReader.readLine(); line!=null; line = bufferedReader.readLine()) {
				fileWriter.write(line.replaceAll(from, to));
				fileWriter.write("\n");
			}
			bufferedReader.close();
			fileReader.close();
			fileWriter.close();
			renameFile(filePath + ".temp", filePath, true, false);
		}
		catch(Exception e) {
			e.printStackTrace();
			try {
				bufferedReader.close();
			}
			catch(Exception ex) {
				
			}
			try {
				fileReader.close();
			}
			catch(Exception ex) {
				
			}
			try {
				fileWriter.close();
			}
			catch(Exception ex) {
				
			}
		}
	}
	
	/**
	 * 读文件
	 * @param fileName
	 * @return
	 */
	public static byte[] readFile(String fileName) {
		FileInputStream inputStream = null;
		try {
			File file = new File(fileName);
			byte[] bytes = new byte[(int)file.length()];
			inputStream = new FileInputStream(file);
			int readLen;
			int offset = 0;
			while((readLen=inputStream.read(bytes, offset, bytes.length-offset))!=-1 && offset<bytes.length) {
				offset += readLen;
			}
			return bytes;
		}
		catch(Exception e) {
			throw new Error(e);
		}
		finally {
			try {
				inputStream.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 读取文件内容
	 * @param fileName
	 * @param charset 空时自动检查
	 * @return
	 */
	public static String readStringFromFile(String fileName, String charset) {
		FileInputStream inputStream = null;
		try {
			//读取前3个字节,检查字符集
			File file = new File(fileName);
			int fileSize = (int)file.length();
			inputStream = new FileInputStream(file);
			byte[] bytes = new byte[fileSize];
			inputStream.read(bytes);
			//Unicode：FF FE, Unicode big_endian：EF FF, UTF-8： EF BB BF
			int skip = 0;
			if(256 + bytes[0]==0xEF && 256 + bytes[1]==0xBB && 256 + bytes[2]==0xBF) {
				charset = "UTF-8";
				skip = 3;
			}
			else if(256 + bytes[0]==0xFF && 256 + bytes[1]==0xFE) {
				charset = "Unicode";
				skip = 2;
			}
			return new String(bytes, skip, fileSize - skip, charset);
		}
		catch(Exception ex) {
			return null;
		}
		finally {
			try {
				inputStream.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 数据保存到文件
	 * @param fileName
	 * @param data
	 * @return
	 */
	public static boolean saveDataToFile(String fileName, byte[] data) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			out.write(data);
			return true;
		}
		catch (Exception ex) {
			Logger.exception(ex);
			deleteFile(fileName, false); //保存失败,删除文件
			return false;
		}
		finally {
			try {
				out.close();
			}
			catch (IOException ex1) {
				
			}
		}
	}
	
	/**
	 * 写入文本内容
	 * @param content
	 * @param writeByteOrderMark
	 * @param file
	 * @return
	 */
	public static boolean saveStringToFile(String fileName, String content, String charset, boolean writeByteOrderMark) {
		FileOutputStream out = null;
		try {
			//Unicode：FF FE, Unicode big_endian：EF FF, UTF-8： EF BB BF
			out = new FileOutputStream(fileName);
			if(writeByteOrderMark) {
				if("UTF-8".equalsIgnoreCase(charset)) {
					out.write(0xEF);
					out.write(0xBB);
					out.write(0xBF);
				}
				else if("UNICODE".equalsIgnoreCase(charset)) {
					out.write(0xFF);
					out.write(0xFE);
				}
			}
			out.write(charset==null ? content.getBytes() : content.getBytes(charset));
			return true;
		}
		catch (Exception ex) {
			Logger.exception(ex);
			deleteFile(fileName, false); //保存失败,删除文件
			return false;
		}
		finally {
			try {
				out.close();
			}
			catch (IOException ex1) {
				
			}
		}
	}
	
	/**
	 * 获取文件字符集
	 * @param fileName
	 * @return
	 */
	public static String getCharset(String fileName) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileName);
			byte[] bytes = new byte[3];
			inputStream.read(bytes);
			String encoding = null;
			//Unicode：FF FE, Unicode big_endian：EF FF, UTF-8： EF BB BF
			if(256 + bytes[0]==0xEF && 256 + bytes[1]==0xBB && 256 + bytes[2]==0xBF) {
				encoding = "UTF-8";
			}
			else if(256 + bytes[0]==0xFF && 256 + bytes[1]==0xFE) {
				encoding = "Unicode";
			}
			else {
				encoding = "GBK";
			}
			return encoding;
		}
		catch(Exception e) {
			return null;
		}
		finally {
			try {
				inputStream.close();
			}
			catch (Exception ex) {
				
			}
		}
	}
	
	/**
	 * 获取文件,并按修改时间降序排序
	 * @param dir
	 * @param desc
	 * @return
	 */
	public static File[] listFilesSortByModified(String dir, final boolean desc) {
		File[] files = new File(dir).listFiles();
		if(files==null || files.length==0) {
			return null;
		}
		//按时间排序,取时间最近的一个
		Arrays.sort(files, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				long time = ((File)arg0).lastModified() - ((File)arg1).lastModified();
				return time == 0 ? 0 : (desc ? (time>0 ? -1 : 1) : (time>0 ? 1 : -1));
			}
		});
		return files;
	}
	
	/**
	 * 获取文件,并按文件名称排序
	 * @param dir
	 * @return
	 */
	public static File[] listFilesSortByName(String dir) {
		File[] files = new File(dir).listFiles();
		if(files==null || files.length==0) {
			return files;
		}
		Arrays.sort(files, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return ((File)arg0).getName().compareToIgnoreCase(((File)arg1).getName());
			}
		});
		return files;
	}
	
	/**
	 * 替换文件扩展名
	 * @param fileName
	 * @param extName
	 * @return
	 */
	public static String replaceExtensionName(String fileName, String newExtensionName) {
		int index = fileName.lastIndexOf('/');
		String dir = fileName.substring(0, index + 1);
		fileName = fileName.substring(index + 1);
		index = fileName.lastIndexOf('.');
		if(index==-1) {
			return fileName + "." + newExtensionName;
		}
		else {
			return dir + fileName.substring(0, index) + "." + newExtensionName;
		}
	}
	
	/**
	 * 文件搜索
	 * @param path
	 * @param fileName
	 * @param fileSearchCallback
	 */
	public static void fileSearch(String path, String fileName, FileSearchCallback fileSearchCallback) {
		fileSearch(new File(path), fileName, fileSearchCallback);
	}
	
	/**
	 * 递归:文件搜索
	 * @param dir
	 * @param fileName
	 * @param fileSearchCallback
	 * @return
	 */
	private static void fileSearch(File dir, String fileName, FileSearchCallback fileSearchCallback) {
		if(!dir.exists()) {
			return;
		}
		File[] files = dir.listFiles();
		for(int i=files.length - 1; i>=0; i--) {
			if(fileName==null || files[i].getName().equals(fileName)) { //找到文件
				fileSearchCallback.onFileFound(files[i]);
			}
			if(files[i].isDirectory()) { //递归搜索子目录
				fileSearch(files[i], fileName, fileSearchCallback);
			}
		}
	}
	
	/**
	 * 获取默认的文件扩展名,格式:所有图片|*.jpg;*.jpeg|
	 * @param isImage
	 * @param isVideo
	 * @return
	 */
	public static String getDefaultFileExtension(boolean isImage, boolean isVideo) {
		if(isImage) {
			return "所有图片|*.jpg;*.jpeg;*.jpe;*.bmp;*.gif;*.png|"; //默认的图片扩展名
		}
		else if(isVideo) {
			return "所有视频|*.avi;*.asx;*.asf;*.mpg;*.wmv;*.3gp;*.mp4;*.mov;*.flv;*.wmv9;*.rm;*.rmvb;*.vob|"; //默认的视频扩展名
		}
		else {
			return "所有文件|*.rar;*.zip;*.doc;*.xls;*.ppt;*.tif;*.txt;*.chm;*.docx;*.xlsx;*.pptx;*.pdf;*.vsd;*.mpp;*.jpg;*.jpeg;*.jpe;*.bmp;*.gif;*.png;*.avi;*.asx;*.asf;*.mpg;*.wmv;*.3gp;*.mp3;*.mp4;*.mov;*.flv;*.wmv9;*.rm;*.rmvb;*.et;*.wps;*.dps;*.pmp;*.bdc|"; //默认的附件扩展名
		}
	}
	
	/**
	 * 校验文件扩展名
	 * @param fileExtension
	 * @param fileName
	 * @return
	 */
	public static boolean validateFileExtension(String fileExtension, String filePath) {
		if(filePath==null || filePath.isEmpty()) {
			return false;
		}
		if(filePath.matches(".*[\0*<>|?]+.*")) {
			return false;
		}
		if(fileExtension==null || fileExtension.equals("") || fileExtension.indexOf("|*.*|")!=-1) {
			return true;
		}
		int index = filePath.lastIndexOf(".");
		if(index==-1) {
			return false;
		}
		boolean valid = false;
		String[] extensions = fileExtension.split("\\|");
		String fileExt = "*" + filePath.substring(index).toLowerCase() + ";";
		for(int i=1; i<extensions.length && !valid; i+=2) {
			valid = (extensions[i] + ";").toLowerCase().indexOf(fileExt)!=-1;
		}
		return valid;
	}

	/**
	 * 获取目录大小
	 * @param dir
	 * @return
	 */
	public static long getDirSize(String dir) {
		if(dir==null) {  
			return 0;  
		} 
		File directory = new File(dir);
		if(!directory.isDirectory()) {  
			return directory.length();  
		}  
		long dirSize = 0;  
		File[] files = directory.listFiles();  
		for(int i=0; i<(files==null ? 0 : files.length); i++) {  
			if(files[i].isFile()) {  
				dirSize += files[i].length();  
			}
			else if (files[i].isDirectory()) {  
				dirSize += files[i].length();  
				dirSize += getDirSize(files[i].getPath()); // 如果遇到目录则通过递归调用继续统计  
			}
		}
		return dirSize;  
	}
	
	/**
	 * 重置文件名
	 * @param fileName
	 * @return
	 */
	public static String retrieveFileName(String fileName) {
		return fileName.replaceAll("[\r\n\t\\\\/\0:*<>|?]", "");
	}
	

	/**
	 * 获取图标URL
	 * @param fileName
	 * @return
	 */
	public static String getIconURL(String fileName) {
		int index = fileName.lastIndexOf('.');
		String iconFile = index==-1 ? null : fileName.substring(index + 1).toLowerCase() + ".png";
		if(iconFile==null || !FileUtils.isExists(Environment.getWebAppPath() + "jeaf/attachment/icons/" + iconFile)) {
			iconFile = "file.png";
		}
		return Environment.getContextPath() + "/jeaf/attachment/icons/" + iconFile;
	}
}