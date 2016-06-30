package com.yuanluesoft.jeaf.directorymanage.pojo;

/**
 * 目录引用
 * @author linchuan
 *
 */
public interface DirectoryLink {
	
	/**
	 * 获取引用的目录ID
	 * @return
	 */
	public long getLinkedDirectoryId();
	
	/**
	 * 设置引用的目录ID
	 * @param linkedDirectoryId
	 */
	public void setLinkedDirectoryId(long linkedDirectoryId);
}