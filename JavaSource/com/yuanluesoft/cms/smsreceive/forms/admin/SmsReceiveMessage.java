package com.yuanluesoft.cms.smsreceive.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 短信接收(sms_receive_message)
 * @author linchuan
 *
 */
public class SmsReceiveMessage extends WorkflowForm {
	private String content; //短信内容
	private String senderNumber; //发送人号码
	private String receiverNumber; //接收号码
	private Timestamp receiveTime; //接收时间
	private long unitId; //受理单位ID
	private String unitName; //受理单位名称
	private String smsBusinessName; //短信业务
	private String replyContent; //答复内容
	private Timestamp replyTime; //答复时间
	
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
	 * @return the replyContent
	 */
	public String getReplyContent() {
		return replyContent;
	}
	/**
	 * @param replyContent the replyContent to set
	 */
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	/**
	 * @return the replyTime
	 */
	public Timestamp getReplyTime() {
		return replyTime;
	}
	/**
	 * @param replyTime the replyTime to set
	 */
	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
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
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the smsBusinessName
	 */
	public String getSmsBusinessName() {
		return smsBusinessName;
	}
	/**
	 * @param smsBusinessName the smsBusinessName to set
	 */
	public void setSmsBusinessName(String smsBusinessName) {
		this.smsBusinessName = smsBusinessName;
	}
}