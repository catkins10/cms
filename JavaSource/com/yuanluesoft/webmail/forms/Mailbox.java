package com.yuanluesoft.webmail.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Mailbox extends ActionForm {
	private String mailboxName; //邮箱名称

	/**
	 * @return the mailboxName
	 */
	public String getMailboxName() {
		return mailboxName;
	}

	/**
	 * @param mailboxName the mailboxName to set
	 */
	public void setMailboxName(String mailboxName) {
		this.mailboxName = mailboxName;
	}
}