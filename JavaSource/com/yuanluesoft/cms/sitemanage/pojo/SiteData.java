package com.yuanluesoft.cms.sitemanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 站点数据
 * @author linchuan
 *
 */
public class SiteData extends Record {
	private long siteId; //站点ID

	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}