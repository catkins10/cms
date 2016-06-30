package com.yuanluesoft.portal.container.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * PORTLET个性设置(portlet_preference)
 * @author linchuan
 *
 */
public class PortletPreference extends Record {
	private long userId; //用户ID
	private long siteId; //站点ID
	private long entityId; //PORTLET实体ID
	private String instanceId; //PORTLET实例ID
	private Set values; //参数值列表
	
	/**
	 * @return the entityId
	 */
	public long getEntityId() {
		return entityId;
	}
	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
	/**
	 * @return the instanceId
	 */
	public String getInstanceId() {
		return instanceId;
	}
	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
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
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the values
	 */
	public Set getValues() {
		return values;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(Set values) {
		this.values = values;
	}
}