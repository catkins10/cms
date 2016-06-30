package com.yuanluesoft.cms.sitemanage.model;

import java.io.Serializable;

/**
 * 按文章隶属站点重建页面
 * @author linchuan
 *
 */
public class RebuildPageByResourceSiteIds implements Serializable {
	private String siteIds; //站点/栏目ID列表

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
}