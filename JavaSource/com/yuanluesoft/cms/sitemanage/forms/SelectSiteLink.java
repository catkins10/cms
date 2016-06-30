package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;

/**
 * 
 * @author linchuan
 *
 */
public class SelectSiteLink extends TreeDialog {
	private String currentApplicationName; //URL参数:当前应用
	private String currentPageName; //URL参数:当前页面

	/**
	 * @return the currentApplicationName
	 */
	public String getCurrentApplicationName() {
		return currentApplicationName;
	}

	/**
	 * @param currentApplicationName the currentApplicationName to set
	 */
	public void setCurrentApplicationName(String currentApplicationName) {
		this.currentApplicationName = currentApplicationName;
	}

	/**
	 * @return the currentPageName
	 */
	public String getCurrentPageName() {
		return currentPageName;
	}

	/**
	 * @param currentPageName the currentPageName to set
	 */
	public void setCurrentPageName(String currentPageName) {
		this.currentPageName = currentPageName;
	}
}