package com.yuanluesoft.jeaf.application.builder.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 索引(application_index)
 * @author linchuan
 *
 */
public class ApplicationIndex extends Record {
	private long formId; //表单ID
	private String fieldIds; //字段ID
	private String fieldNames; //字段名称
	private String fieldDirections; //排序方式,asc/desc
	
	/**
	 * @return the fieldDirections
	 */
	public String getFieldDirections() {
		return fieldDirections;
	}
	/**
	 * @param fieldDirections the fieldDirections to set
	 */
	public void setFieldDirections(String fieldDirections) {
		this.fieldDirections = fieldDirections;
	}
	/**
	 * @return the fieldIds
	 */
	public String getFieldIds() {
		return fieldIds;
	}
	/**
	 * @param fieldIds the fieldIds to set
	 */
	public void setFieldIds(String fieldIds) {
		this.fieldIds = fieldIds;
	}
	/**
	 * @return the fieldNames
	 */
	public String getFieldNames() {
		return fieldNames;
	}
	/**
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(String fieldNames) {
		this.fieldNames = fieldNames;
	}
	/**
	 * @return the formId
	 */
	public long getFormId() {
		return formId;
	}
	/**
	 * @param formId the formId to set
	 */
	public void setFormId(long formId) {
		this.formId = formId;
	}
}