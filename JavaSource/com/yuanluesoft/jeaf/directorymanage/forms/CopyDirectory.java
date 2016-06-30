package com.yuanluesoft.jeaf.directorymanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class CopyDirectory extends ActionForm {
	private long fromDirectoryId; //源目录ID
	private String fromDirectoryName; //源目录名称
	private long toDirectoryId; //目标目录ID
	private String toDirectoryName; //目标目录名称
	private String toDirectoryTypes; //目标目录类型
	private String newDirectoryName; //新目录名称
	
	/**
	 * @return the fromDirectoryId
	 */
	public long getFromDirectoryId() {
		return fromDirectoryId;
	}
	/**
	 * @param fromDirectoryId the fromDirectoryId to set
	 */
	public void setFromDirectoryId(long fromDirectoryId) {
		this.fromDirectoryId = fromDirectoryId;
	}
	/**
	 * @return the fromDirectoryName
	 */
	public String getFromDirectoryName() {
		return fromDirectoryName;
	}
	/**
	 * @param fromDirectoryName the fromDirectoryName to set
	 */
	public void setFromDirectoryName(String fromDirectoryName) {
		this.fromDirectoryName = fromDirectoryName;
	}
	/**
	 * @return the newDirectoryName
	 */
	public String getNewDirectoryName() {
		return newDirectoryName;
	}
	/**
	 * @param newDirectoryName the newDirectoryName to set
	 */
	public void setNewDirectoryName(String newDirectoryName) {
		this.newDirectoryName = newDirectoryName;
	}
	/**
	 * @return the toDirectoryId
	 */
	public long getToDirectoryId() {
		return toDirectoryId;
	}
	/**
	 * @param toDirectoryId the toDirectoryId to set
	 */
	public void setToDirectoryId(long toDirectoryId) {
		this.toDirectoryId = toDirectoryId;
	}
	/**
	 * @return the toDirectoryName
	 */
	public String getToDirectoryName() {
		return toDirectoryName;
	}
	/**
	 * @param toDirectoryName the toDirectoryName to set
	 */
	public void setToDirectoryName(String toDirectoryName) {
		this.toDirectoryName = toDirectoryName;
	}
	/**
	 * @return the toDirectoryTypes
	 */
	public String getToDirectoryTypes() {
		return toDirectoryTypes;
	}
	/**
	 * @param toDirectoryTypes the toDirectoryTypes to set
	 */
	public void setToDirectoryTypes(String toDirectoryTypes) {
		this.toDirectoryTypes = toDirectoryTypes;
	}
}