/*
 * Created on 2005-11-26
 *
 */
package com.yuanluesoft.jeaf.messagecenter.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 消息(messagecenter_message)
 * @author linchuan
 *
 */
public class Message extends Record {
	private long receivePersonId; //接收人ID
	private long senderId; //发送人ID
	private String senderName; //发送人
	private Timestamp sendTime; //通知时间
	private char priority = '0'; //优先级
	private String content; //内容
	private String webLink; //HTTP链接
	private String lastSendMode; //上次通知方式
	private int faildCount; //发送失败次数
	private long sourceRecordId; //源记录ID
	private char bindSendMode = '0'; //是否指定某种发送方式
	private String cyclicMode; //循环通知方式,空/不循环,hour/按小时,day/按天,month/按月
	private int cyclicTime; //循环周期
	private Timestamp cyclicEnd; //截止时间,空表示无限期
	
	/**
	 * @return Returns the content.
	 */
	public java.lang.String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	/**
	 * @return Returns the faildCount.
	 */
	public int getFaildCount() {
		return faildCount;
	}
	/**
	 * @param faildCount The faildCount to set.
	 */
	public void setFaildCount(int faildCount) {
		this.faildCount = faildCount;
	}
	/**
	 * @return Returns the lastSendMode.
	 */
	public java.lang.String getLastSendMode() {
		return lastSendMode;
	}
	/**
	 * @param lastSendMode The lastSendMode to set.
	 */
	public void setLastSendMode(java.lang.String lastSendMode) {
		this.lastSendMode = lastSendMode;
	}
	/**
	 * @return Returns the priority.
	 */
	public char getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(char priority) {
		this.priority = priority;
	}
	/**
	 * @return Returns the receivePersonId.
	 */
	public long getReceivePersonId() {
		return receivePersonId;
	}
	/**
	 * @param receivePersonId The receivePersonId to set.
	 */
	public void setReceivePersonId(long receivePersonId) {
		this.receivePersonId = receivePersonId;
	}
	/**
	 * @return Returns the senderName.
	 */
	public java.lang.String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName The senderName to set.
	 */
	public void setSenderName(java.lang.String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return Returns the sendTime.
	 */
	public java.sql.Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime The sendTime to set.
	 */
	public void setSendTime(java.sql.Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return Returns the webLink.
	 */
	public java.lang.String getWebLink() {
		return webLink;
	}
	/**
	 * @param webLink The webLink to set.
	 */
	public void setWebLink(java.lang.String webLink) {
		this.webLink = webLink;
	}
	/**
	 * @return Returns the sourceRecordId.
	 */
	public long getSourceRecordId() {
		return sourceRecordId;
	}
	/**
	 * @param sourceRecordId The sourceRecordId to set.
	 */
	public void setSourceRecordId(long sourceRecordId) {
		this.sourceRecordId = sourceRecordId;
	}
	/**
	 * @return the bindSendMode
	 */
	public char getBindSendMode() {
		return bindSendMode;
	}
	/**
	 * @param bindSendMode the bindSendMode to set
	 */
	public void setBindSendMode(char bindSendMode) {
		this.bindSendMode = bindSendMode;
	}
	/**
	 * @return the cyclicMode
	 */
	public String getCyclicMode() {
		return cyclicMode;
	}
	/**
	 * @param cyclicMode the cyclicMode to set
	 */
	public void setCyclicMode(String cyclicMode) {
		this.cyclicMode = cyclicMode;
	}
	/**
	 * @return the cyclicTime
	 */
	public int getCyclicTime() {
		return cyclicTime;
	}
	/**
	 * @param cyclicTime the cyclicTime to set
	 */
	public void setCyclicTime(int cyclicTime) {
		this.cyclicTime = cyclicTime;
	}
	/**
	 * @return the cyclicEnd
	 */
	public Timestamp getCyclicEnd() {
		return cyclicEnd;
	}
	/**
	 * @param cyclicEnd the cyclicEnd to set
	 */
	public void setCyclicEnd(Timestamp cyclicEnd) {
		this.cyclicEnd = cyclicEnd;
	}
	/**
	 * @return the senderId
	 */
	public long getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}
}
