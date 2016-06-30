package com.yuanluesoft.cms.infopublic.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 信息公开:信息公开指南(public_guide)
 * @author linchuan
 *
 */
public class PublicGuide extends Record {
	private long directoryId; //主目录ID
	private String guide; //信息公开指南
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
	 * @return the guide
	 */
	public String getGuide() {
		return guide;
	}
	/**
	 * @param guide the guide to set
	 */
	public void setGuide(String guide) {
		this.guide = guide;
	}
}