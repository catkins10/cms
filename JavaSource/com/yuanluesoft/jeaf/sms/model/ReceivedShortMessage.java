package com.yuanluesoft.jeaf.sms.model;

import java.sql.Timestamp;

/**
 * 接收的短信
 * @author linchuan
 *
 */
public class ReceivedShortMessage {
	private String senderNumber; //发送人号码
	private String receiverNumber; //接收人号码
	private String message; //短信内容
	private Timestamp receiveTime; //接收时间
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the receiveTime
	 */
	public Timestamp getReceiveTime() {
		return receiveTime;
	}
	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	/**
	 * @return the senderNumber
	 */
	public String getSenderNumber() {
		return senderNumber;
	}
	/**
	 * @param senderNumber the senderNumber to set
	 */
	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}
	/**
	 * @return the receiverNumber
	 */
	public String getReceiverNumber() {
		return receiverNumber;
	}
	/**
	 * @param receiverNumber the receiverNumber to set
	 */
	public void setReceiverNumber(String receiverNumber) {
		this.receiverNumber = receiverNumber;
	}
}