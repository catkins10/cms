/*
 * Created on 2006-5-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.pojo;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 邮箱(webmail_mailbox)
 * @author linchuan
 *
 */
public class Mailbox extends Record {
	private long userId; //邮件用户ID
	private String mailboxName; //邮箱名称

    private int mailCount;  //邮件总数
    private int newMailCount; 
    private long sizeTotal;
	
	/**
	 * 附件大小
	 * @return
	 */
	public String getSize() {
	    return sizeTotal==0 ? null : StringUtils.getFileSize(sizeTotal);
	}
    /**
     * @return Returns the userId.
     */
    public long getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
    /**
     * @return Returns the mailboxName.
     */
    public java.lang.String getMailboxName() {
        return mailboxName;
    }
    /**
     * @param mailboxName The mailboxName to set.
     */
    public void setMailboxName(java.lang.String mailboxName) {
        this.mailboxName = mailboxName;
    }
    /**
     * @return Returns the mailCount.
     */
    public int getMailCount() {
        return mailCount;
    }
    /**
     * @param mailCount The mailCount to set.
     */
    public void setMailCount(int mailCount) {
        this.mailCount = mailCount;
    }
    /**
     * @return Returns the newMailCount.
     */
    public int getNewMailCount() {
        return newMailCount;
    }
    /**
     * @param newMailCount The newMailCount to set.
     */
    public void setNewMailCount(int newMailCount) {
        this.newMailCount = newMailCount;
    }
    /**
     * @return Returns the sizeTotal.
     */
    public long getSizeTotal() {
        return sizeTotal;
    }
    /**
     * @param sizeTotal The sizeTotal to set.
     */
    public void setSizeTotal(long sizeTotal) {
        this.sizeTotal = sizeTotal;
    }
}
