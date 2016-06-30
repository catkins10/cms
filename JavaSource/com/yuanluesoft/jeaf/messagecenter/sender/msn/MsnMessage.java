/*
 * Created on 2005-11-30
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender.msn;

import java.sql.Timestamp;

/**
 * 
 * @author linchuan
 *
 */
public class MsnMessage {
	public final static char MESSAGE_STATE_NORMAL = '1';	//消息状态:初始
	public final static char MESSAGE_STATE_CALLED = '2';	//消息状态:发起会话
	public final static char MESSAGE_STATE_SENT = '3';	//消息状态:已发送
	
	private long messageId;
	private String content;
	private char priority = '0';
	private String receiverMsnName;
	private long receiverId;
	private Timestamp sendTime;
	private char state = '0';
	private int feedbackDelay;
	private Timestamp created;
	
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
	 * @return Returns the messageId.
	 */
	public long getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId The messageId to set.
	 */
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return Returns the receiverMsnName.
	 */
	public String getReceiverMsnName() {
		return receiverMsnName;
	}
	/**
	 * @param receiverMsnName The receiverMsnName to set.
	 */
	public void setReceiverMsnName(String receiverMsnName) {
		this.receiverMsnName = receiverMsnName;
	}
	/**
	 * @return Returns the sendTime.
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime The sendTime to set.
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return Returns the feedbackDelay.
	 */
	public int getFeedbackDelay() {
		return feedbackDelay;
	}
	/**
	 * @param feedbackDelay The feedbackDelay to set.
	 */
	public void setFeedbackDelay(int feedbackDelay) {
		this.feedbackDelay = feedbackDelay;
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
	 * @return Returns the created.
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return Returns the state.
	 */
	public char getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(char state) {
		this.state = state;
	}
	/**
	 * @return Returns the receiverId.
	 */
	public long getReceiverId() {
		return receiverId;
	}
	/**
	 * @param receiverId The receiverId to set.
	 */
	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}
}
