package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class InsertField extends ActionForm {
	private String applicationName; //URL参数:应用名称
	private String pageName; //URL参数:页面名称,不为空时,按页面来获取字段
	private String recordListName; //URL参数:记录列表名称,不为空时,按记录列表来获取字段
	private boolean linkable; //URL参数:是否可链接
	private boolean privateRecordList; //URL参数:是否私有的记录列表
	private String recordClassName; //URL参数:记录类名称,privateRecordList=true时有效
	private String fieldTitle; //URL参数:字段描述,插入指定的字段时有效
	private String fieldName; //URL参数:字段名称,插入指定的字段时有效
	private String fieldType; //URL参数:字段类型,插入指定的字段时有效
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the recordListName
	 */
	public String getRecordListName() {
		return recordListName;
	}
	/**
	 * @param recordListName the recordListName to set
	 */
	public void setRecordListName(String recordListName) {
		this.recordListName = recordListName;
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
	 * @return the privateRecordList
	 */
	public boolean isPrivateRecordList() {
		return privateRecordList;
	}
	/**
	 * @param privateRecordList the privateRecordList to set
	 */
	public void setPrivateRecordList(boolean privateRecordList) {
		this.privateRecordList = privateRecordList;
	}
	/**
	 * @return the linkable
	 */
	public boolean isLinkable() {
		return linkable;
	}
	/**
	 * @param linkable the linkable to set
	 */
	public void setLinkable(boolean linkable) {
		this.linkable = linkable;
	}
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	/**
	 * @return the recordClassName
	 */
	public String getRecordClassName() {
		return recordClassName;
	}
	/**
	 * @param recordClassName the recordClassName to set
	 */
	public void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
	}
}