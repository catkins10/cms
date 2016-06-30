package com.yuanluesoft.cms.onlineservice.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 网上办事:办理人(onlineservice_item_transactor)
 * @author linchuan
 *
 */
public class OnlineServiceItemTransactor extends Record {
	private long itemId; //办理事项ID
	private long userId; //办理人ID
	private String userName; //办理人
	
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
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}