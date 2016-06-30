package com.yuanluesoft.cms.sitemanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 站点管理:同步设置(cms_directory_synch)
 * @author linchuan
 *
 */
public class WebDirectorySynch extends Record {
	private long directoryId; //目录ID
	private long synchDirectoryId; //同步的目录ID
	
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
	 * @return the synchDirectoryId
	 */
	public long getSynchDirectoryId() {
		return synchDirectoryId;
	}
	/**
	 * @param synchDirectoryId the synchDirectoryId to set
	 */
	public void setSynchDirectoryId(long synchDirectoryId) {
		this.synchDirectoryId = synchDirectoryId;
	}
}