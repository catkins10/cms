/*
 * Created on 2006-5-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.webmail.pojo.Mail;
import com.yuanluesoft.webmail.pojo.MailAttachment;
import com.yuanluesoft.webmail.service.MailboxService;
/**
 * 
 * WEB邮件附件服务
 * @author linchuan
 * 
 */
public class WebMailAttachmentServiceImpl extends AttachmentServiceImpl {
    private DatabaseService databaseService; //数据库访问对象
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl#listAttachments(java.lang.String, java.lang.String, long, javax.servlet.http.HttpServletRequest)
	 */
	public List list(String applicationName, String type, long mainRecordId, boolean iconURLRequired, int max, HttpServletRequest request) throws ServiceException {
		Mail mail = (Mail)databaseService.findRecordById(Mail.class.getName(), mainRecordId, ListUtils.generateList("mailAttachments", ","));
		if(mail==null || mail.getMailboxId()<MailboxService.MAILBOX_INBOX_ID) { //发邮件
			return super.list(applicationName, type, mainRecordId,	iconURLRequired, max, request);
		}
		//收邮件,附件在邮件服务器上,生成自己的下载链接
		if(mail.getMailAttachments()==null || mail.getMailAttachments().isEmpty()) {
			return null;
		}
		List attachments = new ArrayList();
		for(Iterator iterator = mail.getMailAttachments().iterator(); iterator.hasNext();) {
			MailAttachment mailAttachment = (MailAttachment)iterator.next();
			if(mailAttachment.getContentId()!=null) { //contentId不为空,属于正文中的附件
				continue;
			}
			Attachment attachment = new Attachment();
			attachment.setFilePath(mailAttachment.getName()); //路径
			attachment.setName(mailAttachment.getName()); //附件名称
			attachment.setSize(mailAttachment.getSize()); //附件大小
			attachment.setType(type);
			attachment.setApplicationName(applicationName);
			attachment.setRecordId(mainRecordId);
			attachment.setService(this);
			//attachment.setLastModified(0); //文件修改时间
			String url = Environment.getContextPath() + "/webmail/downloadAttachment.shtml?id=" + mailAttachment.getId();
			attachment.setUrlAttachment(url + "&attachment=true"); //下载附件的URL
			attachment.setUrlInline(url); //下载附件的URL
			attachments.add(attachment);
		}
		return attachments;
	}
	
    /**
     * @return Returns the databaseService.
     */
    public DatabaseService getDatabaseService() {
        return databaseService;
    }
    /**
     * @param databaseService The databaseService to set.
     */
    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
}