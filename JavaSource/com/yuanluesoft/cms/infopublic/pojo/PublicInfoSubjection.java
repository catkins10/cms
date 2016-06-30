package com.yuanluesoft.cms.infopublic.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 信息公开:信息所在目录(public_info_subjection)
 * @author linchuan
 *
 */
public class PublicInfoSubjection extends Record {
	private long infoId; //信息ID
	private long directoryId; //目录ID
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
	 * @return the infoId
	 */
	public long getInfoId() {
		return infoId;
	}
	/**
	 * @param infoId the infoId to set
	 */
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}

}
