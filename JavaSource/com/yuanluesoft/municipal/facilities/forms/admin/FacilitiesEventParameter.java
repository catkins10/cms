package com.yuanluesoft.municipal.facilities.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class FacilitiesEventParameter extends ActionForm {
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