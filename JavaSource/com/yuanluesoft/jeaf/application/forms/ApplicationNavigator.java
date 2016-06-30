package com.yuanluesoft.jeaf.application.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationNavigator extends ActionForm {
	private String applicationName; //URL参数:应用名称
	private String viewName; //URL参数:视图名称
	private String nodeId; //URL参数:选中的节点ID
	
	private com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator applicationNavigator; //应用导航
	private String applicationTitle; //应用标题
	private String viewUrl; //打开选定视图的URL
	
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
	/**
	 * @return the applicationNavigator
	 */
	public com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator getApplicationNavigator() {
		return applicationNavigator;
	}
	/**
	 * @param applicationNavigator the applicationNavigator to set
	 */
	public void setApplicationNavigator(
			com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator applicationNavigator) {
		this.applicationNavigator = applicationNavigator;
	}
	/**
	 * @return the applicationTitle
	 */
	public String getApplicationTitle() {
		return applicationTitle;
	}
	/**
	 * @param applicationTitle the applicationTitle to set
	 */
	public void setApplicationTitle(String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}
	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @return the viewUrl
	 */
	public String getViewUrl() {
		return viewUrl;
	}
	/**
	 * @param viewUrl the viewUrl to set
	 */
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
}