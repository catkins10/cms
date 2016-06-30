package com.yuanluesoft.jeaf.application.forms;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 应用视图
 * @author linchuan
 *
 */
public class ApplicationView extends ViewForm {
	private String applicationName; //URL参数:应用名称
	private String viewName; //URL参数:视图名称
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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
}