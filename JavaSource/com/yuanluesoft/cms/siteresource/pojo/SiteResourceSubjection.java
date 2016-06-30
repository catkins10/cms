/*
 * Created on 2007-7-3
 *
 */
package com.yuanluesoft.cms.siteresource.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class SiteResourceSubjection extends Record {
	private long resourceId;
	private long siteId;
	
	/**
	 * @return Returns the resourceId.
	 */
	public long getResourceId() {
		return resourceId;
	}
	/**
	 * @param resourceId The resourceId to set.
	 */
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	/**
	 * @return Returns the siteId.
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId The siteId to set.
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}