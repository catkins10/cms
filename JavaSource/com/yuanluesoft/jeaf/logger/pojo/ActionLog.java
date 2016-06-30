/*
 * Created on 2007-4-20
 *
 */
package com.yuanluesoft.jeaf.logger.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class ActionLog extends Record {
	private String applicationName;
	private String recordType;
	private long recordId;
	private String content;
	private long personId;
	private String personName;
	private Timestamp actionTime;
	private String actionName;
	private String actionType;
	private String ip;
	
	/**
	 * @return Returns the actionName.
	 */
	public String getActionName() {
		return actionName;
	}
	/**
	 * @param actionName The actionName to set.
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	/**
	 * @return Returns the actionTime.
	 */
	public Timestamp getActionTime() {
		return actionTime;
	}
	/**
	 * @param actionTime The actionTime to set.
	 */
	public void setActionTime(Timestamp actionTime) {
		this.actionTime = actionTime;
	}
	/**
	 * @return Returns the applicationName.
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName The applicationName to set.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return Returns the personId.
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return Returns the personName.
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName The personName to set.
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	/**
	 * @return Returns the recordId.
	 */
	public long getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId The recordId to set.
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	/**
	 * @return Returns the recordType.
	 */
	public String getRecordType() {
		return recordType;
	}
	/**
	 * @param recordType The recordType to set.
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	/**
	 * @return Returns the actionType.
	 */
	public String getActionType() {
		return actionType;
	}
	/**
	 * @param actionType The actionType to set.
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
}
