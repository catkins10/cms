package com.yuanluesoft.jeaf.usermanage.forms.admin;

import com.yuanluesoft.cms.templatemanage.forms.Template;

/**
 * 
 * @author linchuan
 *
 */
public class UserPageTemplate extends Template {
	private long userId; //用户ID
	private String applicationNames; //适用的应用名称
	private String applicationTitles; //适用的应用标题
	
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
	 * @return the applicationNames
	 */
	public String getApplicationNames() {
		return applicationNames;
	}

	/**
	 * @param applicationNames the applicationNames to set
	 */
	public void setApplicationNames(String applicationNames) {
		this.applicationNames = applicationNames;
	}

	/**
	 * @return the applicationTitles
	 */
	public String getApplicationTitles() {
		return applicationTitles;
	}

	/**
	 * @param applicationTitles the applicationTitles to set
	 */
	public void setApplicationTitles(String applicationTitles) {
		this.applicationTitles = applicationTitles;
	}
}