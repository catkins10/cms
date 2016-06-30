package com.yuanluesoft.portal.container.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * PORTLET实体:个性设置(portlet_entity_preference)
 * @author linchuan
 *
 */
public class PortletEntityPreference extends Record {
	private long entityId; //PORTLET实体ID
	private String name; //参数名称
	private String value; //参数值
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}