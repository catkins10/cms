package com.yuanluesoft.jeaf.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.tools.zip.ZipOutputStream;

/**
 * 
 * @author linchuan
 *
 */
public class ZipUtils {
	
	/**
	 * 解压缩文件
	 * @param zipFileName
	 * @param outputDirectory
	 * @throws Exception
	 */
	public static void unZip(String zipFileName, String outputDirectory) throws Exception {
		try {
			org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(zipFileName);
			java.util.Enumeration e = zipFile.getEntries();
			org.apache.tools.zip.ZipEntry zipEntry = null;
			outputDirectory = FileUtils.createDirectory(outputDirectory);
			while(e.hasMoreElements()) {
				zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
				//Logger.info("unziping " + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					File f = new File(outputDirectory + zipEntry.getName());
					if(!f.exists()) {
						f.mkdir();
					}
				}
				else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');
					if(fileName.indexOf("/") != -1) {
						FileUtils.createDirectory(outputDirectory + fileName.substring(0, fileName.lastIndexOf("/")));
					}
					File f = new File(outputDirectory + zipEntry.getName());
					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);
					byte[] buffer = new byte[81920];
					int readLen;
					while ( (readLen=in.read(buffer)) != -1) {
						out.write(buffer, 0, readLen);
					}
					out.close();
					in.close();
				}
			}
			zipFile.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 压缩文件
	 * @param zipFileName
	 * @param inputFile
	 * @throws Exception
	 */	
	public static void zip(String zipFileName, String inputFileName) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		zip(out, new File(inputFileName), "");
		out.close();
	}
	
	/**
	 * 压缩
	 * @param out
	 * @param f
	 * @param base
	 * @throws Exception
	 */
	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		}
		else {
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			byte[] buffer = new byte[Math.max(1, Math.min((int)f.length(), 81920))];
			int readLen;
			while((readLen = in.read(buffer))!=-1) {
				out.write(buffer, 0, readLen);
			}
			in.close();
		}
	}
	
	/**
	 * 压缩字符串
	 * @param str
	 * @return
	 */
	public static byte[] compressString(String sourcecode) throws UnsupportedEncodingException {
		ByteArrayOutputStream encodeout = new ByteArrayOutputStream(); 
		try{
			//待压缩的字符 
			ByteArrayInputStream bin=new ByteArrayInputStream(sourcecode.getBytes());
			BufferedReader in = new BufferedReader(new InputStreamReader(bin)); 
			//保存压缩后的字符 
			BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(encodeout)); 
			//开始compress 
			int c; 
			while((c=in.read())!= -1) {
				out.write(c);
			}
			in.close(); 
			bin.close(); 
			out.close(); 
		}
		catch(Exception e) {
			e.printStackTrace(); 
		}
		return encodeout.toByteArray();
	} 

	
	/**
	 * 解压缩字符串
	 * @param compressed
	 * @return
	 */
	public static String uncompressString(byte[] encode) { 
		String decode="";
		try { 
			//开始uncompress 
			ByteArrayInputStream bain = new ByteArrayInputStream(encode);
			BufferedReader bin = new BufferedReader (new InputStreamReader(new GZIPInputStream(bain))); 

			String tmpStr = null;
			while(( tmpStr= bin.readLine())!=null)
			{ 
				decode = decode + tmpStr;
				tmpStr = null;
			}
			bin.close(); 
			bain.close(); 
		}
		catch(Exception e) {
			e.printStackTrace(); 
		} 
		return decode;
	}
}
