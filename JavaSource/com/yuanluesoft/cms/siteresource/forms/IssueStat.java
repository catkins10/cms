package com.yuanluesoft.cms.siteresource.forms;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 发布统计
 * @author linchuan
 *
 */
public class IssueStat extends ViewForm {
	private String siteIds; //指定的站点/栏目ID
	private String siteNames; //指定的站点/栏目名称

	/**
	 * @return the siteIds
	 */
	public String getSiteIds() {
		return siteIds;
	}

	/**
	 * @param siteIds the siteIds to set
	 */
	public void setSiteIds(String siteIds) {
		this.siteIds = siteIds;
	}

	/**
	 * @return the siteNames
	 */
	public String getSiteNames() {
		return siteNames;
	}

	/**
	 * @param siteNames the siteNames to set
	 */
	public void setSiteNames(String siteNames) {
		this.siteNames = siteNames;
	}
}