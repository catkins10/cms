package com.yuanluesoft.jeaf.application.builder.forms;

import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationField;

/**
 * 
 * @author linchuan
 *
 */
public class Field extends ApplicationForm {
	private ApplicationField field = new ApplicationField(); //字段

	/**
	 * @return the field
	 */
	public ApplicationField getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(ApplicationField field) {
		this.field = field;
	}
}