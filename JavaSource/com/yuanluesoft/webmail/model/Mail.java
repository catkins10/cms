/*
 * Created on 2006-5-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.model;

import java.util.List;

/**
 *
 * @author linchuan
 *
 */
public class Mail {
    private MailHeader mailHeader; //邮件头
	private List mailBodies; //正文列表
	private List mailAttachments; //附件列表
	
    /**
     * @return Returns the mailAttachments.
     */
    public List getMailAttachments() {
        return mailAttachments;
    }
    /**
     * @param mailAttachments The mailAttachments to set.
     */
    public void setMailAttachments(List mailAttachments) {
        this.mailAttachments = mailAttachments;
    }
    /**
     * @return Returns the mailBodies.
     */
    public List getMailBodies() {
        return mailBodies;
    }
    /**
     * @param mailBodies The mailBodies to set.
     */
    public void setMailBodies(List mailBodies) {
        this.mailBodies = mailBodies;
    }
    /**
     * @return Returns the mailHeader.
     */
    public MailHeader getMailHeader() {
        return mailHeader;
    }
    /**
     * @param mailHeader The mailHeader to set.
     */
    public void setMailHeader(MailHeader mailHeader) {
        this.mailHeader = mailHeader;
    }
}
