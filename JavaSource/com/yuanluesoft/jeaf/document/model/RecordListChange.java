package com.yuanluesoft.jeaf.document.model;

/**
 * WORD文档,记录列表变动
 * @author chuan
 *
 */
public class RecordListChange {
	private String recordListName; //记录列表名称
	private String parentRecordId; //父记录ID
	private String recordIds; //记录列表包含的记录ID
	
	/**
	 * @return the parentRecordId
	 */
	public String getParentRecordId() {
		return parentRecordId;
	}
	/**
	 * @param parentRecordId the parentRecordId to set
	 */
	public void setParentRecordId(String parentRecordId) {
		this.parentRecordId = parentRecordId;
	}
	/**
	 * @return the recordIds
	 */
	public String getRecordIds() {
		return recordIds;
	}
	/**
	 * @param recordIds the recordIds to set
	 */
	public void setRecordIds(String recordIds) {
		this.recordIds = recordIds;
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
}