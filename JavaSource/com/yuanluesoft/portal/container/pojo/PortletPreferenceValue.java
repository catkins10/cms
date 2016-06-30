package com.yuanluesoft.portal.container.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * PORTLET个性设置:参数(portlet_preference_value)
 * @author linchuan
 *
 */
public class PortletPreferenceValue extends Record {
	private long preferenceId; //个性设置ID
	private String name; //参数名称
	private String value; //参数值
	
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
	 * @return the preferenceId
	 */
	public long getPreferenceId() {
		return preferenceId;
	}
	/**
	 * @param preferenceId the preferenceId to set
	 */
	public void setPreferenceId(long preferenceId) {
		this.preferenceId = preferenceId;
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