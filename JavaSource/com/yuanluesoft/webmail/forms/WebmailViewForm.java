/*
 * Created on 2005-5-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.forms;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author root
 *
 */
public class WebmailViewForm extends ViewForm {
	private long mailboxId; //URL参数:邮箱ID
	private String viewName; //视图名称
	private long moveto; //邮件移动的目标邮箱ID
	
    /**
     * @return Returns the mailboxId.
     */
    public long getMailboxId() {
        return mailboxId;
    }
    /**
     * @param mailboxId The mailboxId to set.
     */
    public void setMailboxId(long mailboxId) {
        this.mailboxId = mailboxId;
    }
    /**
     * @return Returns the viewName.
     */
    public String getViewName() {
        return viewName;
    }
    /**
     * @param viewName The viewName to set.
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
	/**
	 * @return the moveto
	 */
	public long getMoveto() {
		return moveto;
	}
	/**
	 * @param moveto the moveto to set
	 */
	public void setMoveto(long moveto) {
		this.moveto = moveto;
	}
}