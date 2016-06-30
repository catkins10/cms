/*
 * Created on 2006-5-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.model;

import java.sql.Timestamp;

/**
 *
 * @author linchuan
 *
 */
public class MailHeader {
	private String mailId; //邮件ID
	private String mailFrom;
	private String mailTo;
	private String mailCc;
	private String mailBcc;
	private String replyTo;
	private String subject;
	private String contentType;
	private String boundary;
	private Timestamp receiveDate;
	private String mimeVersion;
	private String priority;
	private String contentTransferEncoding;
	private String charset;
	private String type;
	
	/**
	 * @return the boundary
	 */
	public String getBoundary() {
		return boundary;
	}
	/**
	 * @param boundary the boundary to set
	 */
	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}
	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	/**
	 * @return the contentTransferEncoding
	 */
	public String getContentTransferEncoding() {
		return contentTransferEncoding;
	}
	/**
	 * @param contentTransferEncoding the contentTransferEncoding to set
	 */
	public void setContentTransferEncoding(String contentTransferEncoding) {
		this.contentTransferEncoding = contentTransferEncoding;
	}
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the mailBcc
	 */
	public String getMailBcc() {
		return mailBcc;
	}
	/**
	 * @param mailBcc the mailBcc to set
	 */
	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}
	/**
	 * @return the mailCc
	 */
	public String getMailCc() {
		return mailCc;
	}
	/**
	 * @param mailCc the mailCc to set
	 */
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}
	/**
	 * @return the mailFrom
	 */
	public String getMailFrom() {
		return mailFrom;
	}
	/**
	 * @param mailFrom the mailFrom to set
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	/**
	 * @return the mailId
	 */
	public String getMailId() {
		return mailId;
	}
	/**
	 * @param mailId the mailId to set
	 */
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	/**
	 * @return the mailTo
	 */
	public String getMailTo() {
		return mailTo;
	}
	/**
	 * @param mailTo the mailTo to set
	 */
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	/**
	 * @return the mimeVersion
	 */
	public String getMimeVersion() {
		return mimeVersion;
	}
	/**
	 * @param mimeVersion the mimeVersion to set
	 */
	public void setMimeVersion(String mimeVersion) {
		this.mimeVersion = mimeVersion;
	}
	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @return the replyTo
	 */
	public String getReplyTo() {
		return replyTo;
	}
	/**
	 * @param replyTo the replyTo to set
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
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
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the receiveDate
	 */
	public Timestamp getReceiveDate() {
		return receiveDate;
	}
	/**
	 * @param receiveDate the receiveDate to set
	 */
	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}
}