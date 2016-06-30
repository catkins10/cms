package com.yuanluesoft.jeaf.messagecenter.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SendMessage extends ActionForm {
	private String content;
	private char priority = '0';
	private String priorityTitle;
	private String receivePersonIds;
	private String receivePersonNames;
	private String senderName;
	private Timestamp sendTime;
	private String webLink;
	private long sourceRecordId;
	private String bindSendMode; //绑定某种发送方式
	private String bindSendModeName;
	
	/**
	 * @return the bindSendMode
	 */
	public String getBindSendMode() {
		return bindSendMode;
	}
	/**
	 * @param bindSendMode the bindSendMode to set
	 */
	public void setBindSendMode(String bindSendMode) {
		this.bindSendMode = bindSendMode;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the priority
	 */
	public char getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(char priority) {
		this.priority = priority;
	}
	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return the sendTime
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the sourceRecordId
	 */
	public long getSourceRecordId() {
		return sourceRecordId;
	}
	/**
	 * @param sourceRecordId the sourceRecordId to set
	 */
	public void setSourceRecordId(long sourceRecordId) {
		this.sourceRecordId = sourceRecordId;
	}
	/**
	 * @return the webLink
	 */
	public String getWebLink() {
		return webLink;
	}
	/**
	 * @param webLink the webLink to set
	 */
	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}
	/**
	 * @return the priorityTitle
	 */
	public String getPriorityTitle() {
		return priorityTitle;
	}
	/**
	 * @param priorityTitle the priorityTitle to set
	 */
	public void setPriorityTitle(String priorityTitle) {
		this.priorityTitle = priorityTitle;
	}
	/**
	 * @return the bindSendModeName
	 */
	public String getBindSendModeName() {
		return bindSendModeName;
	}
	/**
	 * @param bindSendModeName the bindSendModeName to set
	 */
	public void setBindSendModeName(String bindSendModeName) {
		this.bindSendModeName = bindSendModeName;
	}
	/**
	 * @return the receivePersonIds
	 */
	public String getReceivePersonIds() {
		return receivePersonIds;
	}
	/**
	 * @param receivePersonIds the receivePersonIds to set
	 */
	public void setReceivePersonIds(String receivePersonIds) {
		this.receivePersonIds = receivePersonIds;
	}
	/**
	 * @return the receivePersonNames
	 */
	public String getReceivePersonNames() {
		return receivePersonNames;
	}
	/**
	 * @param receivePersonNames the receivePersonNames to set
	 */
	public void setReceivePersonNames(String receivePersonNames) {
		this.receivePersonNames = receivePersonNames;
	}
}
