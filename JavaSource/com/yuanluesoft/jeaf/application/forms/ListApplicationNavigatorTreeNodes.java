package com.yuanluesoft.jeaf.application.forms;

import com.yuanluesoft.jeaf.tree.forms.TreeForm;

/**
 * 获取应用导航目录树子节点
 * @author linchuan
 *
 */
public class ListApplicationNavigatorTreeNodes extends TreeForm {
	private String applicationName; //URL参数:应用名称

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
}