package com.yuanluesoft.cms.capture.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 抓取任务:字段配置(cms_capture_field)
 * @author linchuan
 *
 */
public class CmsCaptureField extends Record {
	private long taskId; //任务ID
	private int isListPage; //是否列表页面,列表页面/1,记录页面/0
	private String fieldName; //字段名称
	private String fieldBegin; //开始位置
	private String fieldEnd; //结束位置
	private String arraySeparator; //数组分隔符
	private String fieldFormat; //格式,日期格式化
	private double priority; //优先级
	private String value; //设定值
	
	//附加属性,配置时使用
	private String fieldTitle; //字段标题
	private String fieldType; //字段类型
	private String componentClassName; //组成部分类名称
	
	/**
	 * @return the fieldBegin
	 */
	public String getFieldBegin() {
		return fieldBegin;
	}
	/**
	 * @param fieldBegin the fieldBegin to set
	 */
	public void setFieldBegin(String fieldBegin) {
		this.fieldBegin = fieldBegin;
	}
	/**
	 * @return the fieldEnd
	 */
	public String getFieldEnd() {
		return fieldEnd;
	}
	/**
	 * @param fieldEnd the fieldEnd to set
	 */
	public void setFieldEnd(String fieldEnd) {
		this.fieldEnd = fieldEnd;
	}
	/**
	 * @return the fieldFormat
	 */
	public String getFieldFormat() {
		return fieldFormat;
	}
	/**
	 * @param fieldFormat the fieldFormat to set
	 */
	public void setFieldFormat(String fieldFormat) {
		this.fieldFormat = fieldFormat;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}
	/**
	 * @return the taskId
	 */
	public long getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the isListPage
	 */
	public int getIsListPage() {
		return isListPage;
	}
	/**
	 * @param isListPage the isListPage to set
	 */
	public void setIsListPage(int isListPage) {
		this.isListPage = isListPage;
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
	/**
	 * @return the arraySeparator
	 */
	public String getArraySeparator() {
		return arraySeparator;
	}
	/**
	 * @param arraySeparator the arraySeparator to set
	 */
	public void setArraySeparator(String arraySeparator) {
		this.arraySeparator = arraySeparator;
	}
	/**
	 * @return the fieldTitle
	 */
	public String getFieldTitle() {
		return fieldTitle;
	}
	/**
	 * @param fieldTitle the fieldTitle to set
	 */
	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	/**
	 * @return the componentClassName
	 */
	public String getComponentClassName() {
		return componentClassName;
	}
	/**
	 * @param componentClassName the componentClassName to set
	 */
	public void setComponentClassName(String componentClassName) {
		this.componentClassName = componentClassName;
	}
}