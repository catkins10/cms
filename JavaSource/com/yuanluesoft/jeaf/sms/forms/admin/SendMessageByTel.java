package com.yuanluesoft.jeaf.sms.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SendMessageByTel extends ActionForm {
	private String receiverNumbers; //接收人手机号码
	private String message; //短信内容
	private Timestamp sendTime; //指定发送时间
	
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
	 * @return the receiverNumbers
	 */
	public String getReceiverNumbers() {
		return receiverNumbers;
	}
	/**
	 * @param receiverNumbers the receiverNumbers to set
	 */
	public void setReceiverNumbers(String receiverNumbers) {
		this.receiverNumbers = receiverNumbers;
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
}