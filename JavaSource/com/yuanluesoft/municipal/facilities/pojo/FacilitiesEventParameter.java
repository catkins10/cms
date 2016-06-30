package com.yuanluesoft.municipal.facilities.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 参数配置(facilities_event_parameter)
 * @author linchuan
 *
 */
public class FacilitiesEventParameter extends Record {
	private String eventNumberFormat; //编号规则
	/**
	 * @return the eventNumberFormat
	 */
	public String getEventNumberFormat() {
		return eventNumberFormat;
	}
	/**
	 * @param eventNumberFormat the eventNumberFormat to set
	 */
	public void setEventNumberFormat(String eventNumberFormat) {
		this.eventNumberFormat = eventNumberFormat;
	}
}