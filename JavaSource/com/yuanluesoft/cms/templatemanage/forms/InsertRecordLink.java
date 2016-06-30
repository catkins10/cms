package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class InsertRecordLink extends ActionForm {
	private String applicationName; //URL参数:应用名称
	private String recordListName; //URL参数:记录列表名称
	private boolean privateRecordList; //URL参数:是否私有的记录列表
	private String recordClassName; //URL参数:记录类名称,privateRecordList=true时有效
	
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