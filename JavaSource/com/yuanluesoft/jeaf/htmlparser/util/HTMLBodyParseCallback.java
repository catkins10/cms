package com.yuanluesoft.jeaf.htmlparser.util;

/**
 * 
 * @author linchuan
 *
 */
public interface HTMLBodyParseCallback {

	/**
	 * 由回调程序自行下载HTML资源,返回文件名称,返回null时采用默认方式处理
	 * @param url
	 * @return
	 */
	public String downloadResource(String url); 
}