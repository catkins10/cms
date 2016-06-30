/*
 * Created on 2006-5-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.model;

/**
 *
 * @author linchuan
 *
 */
public class MailInfo {
    private String mailId; //邮件ID
    private int mailSize; //邮件大小
    
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
	 * @return the mailSize
	 */
	public int getMailSize() {
		return mailSize;
	}
	/**
	 * @param mailSize the mailSize to set
	 */
	public void setMailSize(int mailSize) {
		this.mailSize = mailSize;
	}
}