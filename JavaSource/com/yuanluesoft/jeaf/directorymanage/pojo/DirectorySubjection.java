package com.yuanluesoft.jeaf.directorymanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 目录隶属关系
 * @author linchuan
 *
 */
public class DirectorySubjection extends Record {
	private long directoryId; //目录ID
	private long parentDirectoryId; //上级目录ID
	
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the parentDirectoryId
	 */
	public long getParentDirectoryId() {
		return parentDirectoryId;
	}
	/**
	 * @param parentDirectoryId the parentDirectoryId to set
	 */
	public void setParentDirectoryId(long parentDirectoryId) {
		this.parentDirectoryId = parentDirectoryId;
	}
}
