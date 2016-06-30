package com.yuanluesoft.cms.pagebuilder.pojo;

/**
 * 静态页面生成任务:按记录(cms_page_task_by_record)
 * @author linchuan
 *
 */
public class StaticPageTaskByRecord extends StaticPageTask {
	private long recordId; //记录ID
	private String recordClassName; //记录类名称
	private String modifyAction; //记录操作
	private String recordDetail; //记录明细

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "rebuild by object " + recordClassName + ", id is " + recordId + ", modify action is " + modifyAction;
	}
	
	/**
	 * @return the modifyAction
	 */
	public String getModifyAction() {
		return modifyAction;
	}
	/**
	 * @param modifyAction the modifyAction to set
	 */
	public void setModifyAction(String modifyAction) {
		this.modifyAction = modifyAction;
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
	/**
	 * @return the recordDetail
	 */
	public String getRecordDetail() {
		return recordDetail;
	}
	/**
	 * @param recordDetail the recordDetail to set
	 */
	public void setRecordDetail(String recordDetail) {
		this.recordDetail = recordDetail;
	}
	/**
	 * @return the recordId
	 */
	public long getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
}