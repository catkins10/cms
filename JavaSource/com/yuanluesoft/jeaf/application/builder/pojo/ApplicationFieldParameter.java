package com.yuanluesoft.jeaf.application.builder.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 字段:参数(application_field_parameter)
 * @author linchuan
 *
 */
public class ApplicationFieldParameter extends Record {
	private long fieldId; //字段ID
	private String parameterName; //参数名称
	private String parameterValue; //参数值
	
	/**
	 * @return the fieldId
	 */
	public long getFieldId() {
		return fieldId;
	}
	/**
	 * @param fieldId the fieldId to set
	 */
	public void setFieldId(long fieldId) {
		this.fieldId = fieldId;
	}
	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}
	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	/**
	 * @return the parameterValue
	 */
	public String getParameterValue() {
		return parameterValue;
	}
	/**
	 * @param parameterValue the parameterValue to set
	 */
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
}