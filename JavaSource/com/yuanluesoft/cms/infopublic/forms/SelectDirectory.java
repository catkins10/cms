/*
 * Created on 2007-7-8
 *
 */
package com.yuanluesoft.cms.infopublic.forms;

import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;

/**
 * 
 * @author linchuan
 *
 */
public class SelectDirectory extends TreeDialog {
	private boolean countPublicInfo; //是否需要统计信息数量
	private boolean displayRecentUsed; //是否显示最近使用的目录
	private boolean displayDirectoryCode; //是否显示目录代码
	
	/**
	 * @return the countPublicInfo
	 */
	public boolean isCountPublicInfo() {
		return countPublicInfo;
	}
	/**
	 * @param countPublicInfo the countPublicInfo to set
	 */
	public void setCountPublicInfo(boolean countPublicInfo) {
		this.countPublicInfo = countPublicInfo;
	}
	/**
	 * @return the displayRecentUsed
	 */
	public boolean isDisplayRecentUsed() {
		return displayRecentUsed;
	}
	/**
	 * @param displayRecentUsed the displayRecentUsed to set
	 */
	public void setDisplayRecentUsed(boolean displayRecentUsed) {
		this.displayRecentUsed = displayRecentUsed;
	}
	/**
	 * @return the displayDirectoryCode
	 */
	public boolean isDisplayDirectoryCode() {
		return displayDirectoryCode;
	}
	/**
	 * @param displayDirectoryCode the displayDirectoryCode to set
	 */
	public void setDisplayDirectoryCode(boolean displayDirectoryCode) {
		this.displayDirectoryCode = displayDirectoryCode;
	}
}
