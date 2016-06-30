package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;

/**
 * 
 * @author linchuan
 *
 */
public class Select extends TreeDialog {
	private String popedomFilters; //权限过滤
	private boolean displayRecentUsed; //是否显示最近使用的栏目

	/**
	 * @return the popedomFilters
	 */
	public String getPopedomFilters() {
		return popedomFilters;
	}

	/**
	 * @param popedomFilters the popedomFilters to set
	 */
	public void setPopedomFilters(String popedomFilters) {
		this.popedomFilters = popedomFilters;
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
}