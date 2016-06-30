package com.yuanluesoft.jeaf.directorymanage.pojo;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 目录权限
 * @author linchuan
 *
 */
public class DirectoryPopedom extends Record {
	private long directoryId; //目录ID
	private long userId; //用户ID,用户、部门、角色和网上注册用户
	private String userName; //用户名
	private String popedomName; //权限
	private char isInherit = '0'; //是否从上级目录继承
	
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
	 * @return the isInherit
	 */
	public char getIsInherit() {
		return isInherit;
	}
	/**
	 * @param isInherit the isInherit to set
	 */
	public void setIsInherit(char isInherit) {
		this.isInherit = isInherit;
	}
	/**
	 * @return the popedomName
	 */
	public String getPopedomName() {
		return popedomName;
	}
	/**
	 * @param popedomName the popedomName to set
	 */
	public void setPopedomName(String popedomName) {
		this.popedomName = popedomName;
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
