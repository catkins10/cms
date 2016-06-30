package com.yuanluesoft.jeaf.tools.deployment;

import java.io.File;

/**
 * 
 * @author linchuan
 *
 */
public class DeploymentJavaSource {
	private static String source = new File("c:/workspace/cms/JavaSource").getAbsolutePath();
	private static String target = "c:/workspace/cms/JavaSource1";

	public static void main(String[] agrs) throws Exception {
		compare(new File(source));
	}
	
	private static void compare(File dir) {
		if(!dir.exists()) {
			return;
		}
		File[] files = dir.listFiles();
		for(int i=files.length - 1; i>=0; i--) {
			String path = files[i].getAbsolutePath().substring(source.length());
			//检查目标目录中是否有同名文件
			if(!new File(target + path).exists()) { //没找到文件
				System.out.println("                <exclude name=\"" + path.substring(1).replaceAll("\\\\", "/") + (files[i].isDirectory() ? "/**" : "") + "\"/>");
			}
			else if(files[i].isDirectory()) { //递归搜索子目录
				compare(files[i]);
			}
		}
	}
}