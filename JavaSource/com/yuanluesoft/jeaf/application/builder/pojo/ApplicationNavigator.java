package com.yuanluesoft.jeaf.application.builder.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 应用:导航栏(application_navigator)
 * @author linchuan
 *
 */
public class ApplicationNavigator extends Record {
	private long applicationId; //应用ID
	private String label; //名称
	private long viewId; //视图ID
	private String viewName; //视图名称
	private String url; //链接,视图ID为0时有效
	private String accessPrivilege; //访问权限
	private float priority; //优先级
	
	/**
	 * @return the applicationId
	 */
	public long getApplicationId() {
		return applicationId;
	}
	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the viewId
	 */
	public long getViewId() {
		return viewId;
	}
	/**
	 * @param viewId the viewId to set
	 */
	public void setViewId(long viewId) {
		this.viewId = viewId;
	}
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @return the accessPrivilege
	 */
	public String getAccessPrivilege() {
		return accessPrivilege;
	}
	/**
	 * @param accessPrivilege the accessPrivilege to set
	 */
	public void setAccessPrivilege(String accessPrivilege) {
		this.accessPrivilege = accessPrivilege;
	}
}