/*
 * Created on 2005-4-9
 *
 */
package com.yuanluesoft.jeaf.view.viewcustomize.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class ViewCustom extends Record {
	private String applicationName;
	private String viewName;
	private long userId;
	private String custom;
	
	/**
	 * @return Returns the custom.
	 */
	public String getCustom() {
		return custom;
	}
	/**
	 * @param custom The custom to set.
	 */
	public void setCustom(String custom) {
		this.custom = custom;
	}
	/**
	 * @return Returns the applicationName.
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName The applicationName to set.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return Returns the userId.
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return Returns the viewName.
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName The viewName to set.
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
}