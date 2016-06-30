package com.yuanluesoft.cms.monitor.sms.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.cms.monitor.pojo.MonitorRecord;

/**
 * 监察:短信接收(monitor_sms_receive)
 * @author linchuan
 *
 */
public class MonitorSmsReceive extends MonitorRecord {
	private String content; //短信内容
	private String senderNumber; //发送人号码
	private String receiverNumber; //接收号码
	private Timestamp receiveTime; //接收时间
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
}