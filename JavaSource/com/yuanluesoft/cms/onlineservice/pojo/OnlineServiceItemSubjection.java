package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 网上办事:办理事项隶属目录(onlineservice_item_subjection)
 * @author linchuan
 *
 */
public class OnlineServiceItemSubjection extends Record {
	private long itemId; //办理事项ID
	private long directoryId; //隶属目录ID
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
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
}
