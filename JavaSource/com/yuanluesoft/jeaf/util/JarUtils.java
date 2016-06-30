package com.yuanluesoft.jeaf.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

/**
 * 
 * @author linchuan
 *
 */
public class JarUtils {
	private static final int BUFFER_SIZE = 1024*2; //2k
	
	/**
	 * 压缩文件夹及其子文件夹
	 * @param source String 源文件夹,如: d:/tmp
	 * @param dest String 目标文件,如: e:/tmp.jar
	 * @throws IOException
	 */
	public static void compressFolder(String source, String dest)throws IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
		JarOutputStream jos = new JarOutputStream(new FileOutputStream(dest), manifest);
		jos.setLevel(Deflater.DEFAULT_COMPRESSION);
		compressJarFolder(jos, new File(source),"");
		jos.close();
	}

	/**
	 * 压缩文件夹
	 * @param jos
	 * @param f
	 * @param base
	 * @throws IOException
	 */
	private static void compressJarFolder(JarOutputStream jos, File f, String base) throws IOException {
		if(f.isFile()){
			compressJarFile(jos, f, base);
		}
		else if(f.isDirectory()) {
			compressDirEntry(jos, f, base);
			String[] fileList = f.list();
			for(int i=0; i<fileList.length; i++) {
				String newSource = f.getAbsolutePath() + File.separator + fileList[i];
				File newFile = new File(newSource);
				String newBase = base + "/" + f.getName() + "/" + newFile.getName();
				if(base.equals("")) {
					newBase = newFile.getName(); //f.getName()+"/"+newFile.getName();
				}
				else {
					newBase = base + "/" + newFile.getName();
				}
				compressJarFolder(jos, newFile, newBase);
			}
		}
	}

	/**
	 * 压缩单个文件
	 * @param jos
	 * @param f
	 * @param base
	 * @throws IOException
	 */
	private static void compressJarFile(JarOutputStream jos, File f, String base)throws IOException{
		jos.putNextEntry(new ZipEntry(base));
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(f));
		byte[] data = new byte[BUFFER_SIZE];
		int iRead = 0;
		while((iRead = bin.read(data)) != -1) {
			jos.write(data,0,iRead);
		}
		bin.close();
		jos.closeEntry();
	}

	/**
	 * 压缩单个文件到JAR文件中
	 * @param sourceFile
	 * @param jarFile
	 * @throws IOException
	 */
	public static void compressFile(String sourceFile, String jarFile)throws IOException{
		File f = new File(sourceFile);
		String base = f.getName();
		JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
		jos.putNextEntry(new ZipEntry(base));
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(f));
		byte[] data = new byte[BUFFER_SIZE];
		while ((bin.read(data)) != -1) {
			jos.write(data);
		}
		bin.close();
		jos.closeEntry();
		jos.close();
	}

	/**
	 * 压缩空文件夹
	 * @param jos
	 * @param f
	 * @param base
	 * @throws IOException
	 */
	private static void compressDirEntry(JarOutputStream jos, File f, String base)throws IOException{
		jos.putNextEntry(new ZipEntry(base + "/"));
		jos.closeEntry();
	}
}