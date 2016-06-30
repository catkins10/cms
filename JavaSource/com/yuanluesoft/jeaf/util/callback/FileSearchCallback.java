package com.yuanluesoft.jeaf.util.callback;

import java.io.File;

/**
 * 文件搜索回调
 * @author linchuan
 *
 */
public interface FileSearchCallback {

	/**
	 * 找到文件
	 * @param file
	 */
	public void onFileFound(File file);
}