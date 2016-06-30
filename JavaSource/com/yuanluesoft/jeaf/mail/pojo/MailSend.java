package com.yuanluesoft.jeaf.mail.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 邮件服务:发送记录(mail_send)
 * @author linchuan
 *
 */
public class MailSend extends Record {
	private long orgId; //单位ID
	private String orgName; //单位名称
	private String mailAddress; //邮箱
	private String subject; //主题
	private String receivers; //接收人
	private String body; //内容
	private Timestamp sent; //发送时间
	private long senderId; //发送人ID
	private String sender; //发送人
	
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the receivers
	 */
	public String getReceivers() {
		return receivers;
	}
	/**
	 * @param receivers the receivers to set
	 */
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
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
	/**
	 * @return the sent
	 */
	public Timestamp getSent() {
		return sent;
	}
	/**
	 * @param sent the sent to set
	 */
	public void setSent(Timestamp sent) {
		this.sent = sent;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}