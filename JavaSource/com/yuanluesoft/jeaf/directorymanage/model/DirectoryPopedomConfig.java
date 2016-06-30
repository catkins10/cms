package com.yuanluesoft.jeaf.directorymanage.model;

/**
 * 目录权限配置
 * @author linchuan
 *
 */
public class DirectoryPopedomConfig {
	private String popedomName; //名称
	private String popedomTitle; //标题
	private String userIds; //有权限的用户ID列表
	private String userNames; //有权限的用户姓名列表
	private String inheritUserNames; //继承来的用户列表
	
	/**
	 * @return the popedomTitle
	 */
	public String getPopedomTitle() {
		return popedomTitle;
	}
	/**
	 * @param popedomTitle the popedomTitle to set
	 */
	public void setPopedomTitle(String popedomTitle) {
		this.popedomTitle = popedomTitle;
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
	 * @return the userIds
	 */
	public String getUserIds() {
		return userIds;
	}
	/**
	 * @param userIds the userIds to set
	 */
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	/**
	 * @return the userNames
	 */
	public String getUserNames() {
		return userNames;
	}
	/**
	 * @param userNames the userNames to set
	 */
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	/**
	 * @return the inheritUserNames
	 */
	public String getInheritUserNames() {
		return inheritUserNames;
	}
	/**
	 * @param inheritUserNames the inheritUserNames to set
	 */
	public void setInheritUserNames(String inheritUserNames) {
		this.inheritUserNames = inheritUserNames;
	}
}