/*
 * Created on 2005-5-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 邮件正文(webmail_mail_body)
 * @author linchuan
 *
 */
public class MailBody extends Record {
	private long mailId; //邮件记录ID
	private String body; //正文
	
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
	 * @return the mailId
	 */
	public long getMailId() {
		return mailId;
	}
	/**
	 * @param mailId the mailId to set
	 */
	public void setMailId(long mailId) {
		this.mailId = mailId;
	}
}