package com.yuanluesoft.jeaf.form.model;

import com.yuanluesoft.jeaf.business.model.Field;

/**
 * 动态表单字段
 * @author linchuan
 *
 */
public class DynamicFormField {
	private Field fieldDefine; //字段定义
	private Object value; //值
	
	public DynamicFormField(Field fieldDefine, Object value) {
		super();
		this.fieldDefine = fieldDefine;
		this.value = value;
	}
	
	/**
	 * @return the fieldDefine
	 */
	public Field getFieldDefine() {
		return fieldDefine;
	}
	/**
	 * @param fieldDefine the fieldDefine to set
	 */
	public void setFieldDefine(Field fieldDefine) {
		this.fieldDefine = fieldDefine;
	}
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}