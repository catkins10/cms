package com.yuanluesoft.cms.siteresource.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 站点资源:置顶(cms_resource_top)
 * @author linchuan
 *
 */
public class SiteResourceTop extends Record {
	private long resourceId; //资源ID
	private long siteId; //站点/栏目ID
	private Date expire; //有效期
	private Timestamp created; //创建时间
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the expire
	 */
	public Date getExpire() {
		return expire;
	}
	/**
	 * @param expire the expire to set
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}
	/**
	 * @return the resourceId
	 */
	public long getResourceId() {
		return resourceId;
	}
	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
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