/*
 * Created on 2006-5-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.webmail.pojo.Mailbox;
import com.yuanluesoft.webmail.service.MailboxService;
import com.yuanluesoft.webmail.service.WebMailService;

/**
 *
 * @author linchuan
 *
 */
public class MailboxServiceImpl extends ViewServiceImpl implements MailboxService {
    private WebMailService webMailService; //邮件服务
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.view.model.ViewPackage, java.lang.String, java.lang.String, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
    protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("receiveMailbox".equals(view.getName())) {
			return listMailboxes(true, true, sessionInfo);
		}
		else {
			return listMailboxes(false, true, sessionInfo);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailboxService#listMailboxes(boolean, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listMailboxes(boolean inboxOnly, boolean detail, SessionInfo sessionInfo) throws ServiceException {
	    List mailboxes = getDatabaseService().findRecordsByKey(com.yuanluesoft.webmail.pojo.Mailbox.class.getName(), "userId", new Long(sessionInfo.getUserId()));
        if(mailboxes==null) {
            mailboxes = new ArrayList();
        }
        //添加收件箱到邮箱列表
        mailboxes.add(0, getMailbox(MAILBOX_INBOX_ID, sessionInfo));
        if(!inboxOnly) {
            //添加发件箱到邮箱列表
            mailboxes.add(1, getMailbox(MAILBOX_OUTBOX_ID, sessionInfo));
            //添加草稿箱到邮箱列表
            mailboxes.add(2, getMailbox(MAILBOX_DRAFT_ID, sessionInfo));
        }
        //添加回收站到邮箱列表
        mailboxes.add(getMailbox(MAILBOX_RECYCLE_ID, sessionInfo));
        //添加垃圾箱到邮箱列表
        mailboxes.add(getMailbox(MAILBOX_TRASH_ID, sessionInfo));
        if(detail) { //获取明细
            List details = getDatabaseService().findRecordsByHql("select Mail.mailboxId, count(Mail), sum(Mail.newMail), sum(Mail.size) from Mail Mail where Mail.userId=" + sessionInfo.getUserId() + " and Mail.mailboxId>0 group by Mail.mailboxId");
            if(details!=null) {
                for(Iterator iterator = details.iterator(); iterator.hasNext();) {
                    Object[] detailInfo = (Object[])iterator.next();
                    Mailbox mailbox = (Mailbox)ListUtils.findObjectByProperty(mailboxes, "id", detailInfo[0]);
                    if(mailbox==null) {
                    	continue;
                    }
                    mailbox.setMailCount(((Number)detailInfo[1]).intValue());
                    mailbox.setNewMailCount(((Number)detailInfo[2]).intValue());
                    mailbox.setSizeTotal(((Number)detailInfo[3]).longValue());
                }
            }
        }
        return mailboxes;
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailboxService#deleteMailbox(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteMailbox(long mailboxId, SessionInfo sessionInfo) throws ServiceException {
	    Mailbox mailbox = loadMailbox(mailboxId, sessionInfo);
        if(mailbox!=null) {
            List mailIds = getDatabaseService().findRecordsByHql("select Mail.id from Mail Mail where Mail.mailboxId=" + mailboxId);
            if(mailIds!=null) {
                for(Iterator iterator = mailIds.iterator(); iterator.hasNext();) {
                    Long mailId = (Long)iterator.next();
                    webMailService.moveMail(mailId.longValue(), MAILBOX_INBOX_ID, sessionInfo); //将已删除邮箱的邮件移动到收件箱
                }
            }
            getDatabaseService().deleteRecord(mailbox); //删除邮箱
        }
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailboxService#loadMailbox(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Mailbox loadMailbox(long mailboxId, SessionInfo sessionInfo) throws ServiceException {
		Mailbox mailbox = getMailbox(mailboxId, sessionInfo);
        if(mailbox==null) {
            return null;
        }
        Object[] detail = (Object[])getDatabaseService().findRecordByHql("select count(Mail), sum(Mail.newMail), sum(Mail.size) from Mail Mail where Mail.userId=" + sessionInfo.getUserId() + " and Mail.mailboxId=" + mailboxId);
        mailbox.setMailCount(detail[0]==null ? 0 : ((Number)detail[0]).intValue());
        mailbox.setNewMailCount(detail[1]==null ? 0 : ((Number)detail[1]).intValue());
        mailbox.setSizeTotal(detail[2]==null ? 0 : ((Number)detail[2]).longValue());
        return mailbox;
    }
    
    /**
     * 获取邮箱
     * @param mailboxId
     * @param sessionInfo
     * @return
     * @throws ServiceException
     */
    private Mailbox getMailbox(long mailboxId, SessionInfo sessionInfo) throws ServiceException {
        String name;
        switch((int)mailboxId) {
        case MAILBOX_INBOX_ID:
            name = "收件箱";
            break;
            
        case MAILBOX_TRASH_ID:
            name = "垃圾邮件";
            break;
            
        case MAILBOX_RECYCLE_ID:
            name = "回收站";
            break;
            
        case MAILBOX_OUTBOX_ID:
            name = "发件箱";
            break;
            
        case MAILBOX_DRAFT_ID:
            name = "草稿箱";
            break;
            
        default:
            return (Mailbox)getDatabaseService().findRecordByHql("from Mailbox Mailbox where Mailbox.userId=" + sessionInfo.getUserId() + " and Mailbox.id=" + mailboxId);
        }
        Mailbox mailbox = new Mailbox();
        mailbox.setId(mailboxId);
        mailbox.setMailboxName(name);
        return mailbox;        
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailboxService#addMailbox(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Mailbox addMailbox(String name, SessionInfo sessionInfo) throws ServiceException {
		Mailbox mailbox = new Mailbox();
		mailbox.setId(UUIDLongGenerator.generateId()); //ID
		mailbox.setUserId(sessionInfo.getUserId()); //邮件用户ID
		mailbox.setMailboxName(name); //邮箱名称
		getDatabaseService().saveRecord(mailbox);
		return mailbox;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailboxService#renameMailbox(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Mailbox renameMailbox(long mailboxId, String name, SessionInfo sessionInfo) throws ServiceException {
		if(mailboxId<20) { //预置邮箱,不允许改名
			return null;
		}
		Mailbox mailbox = getMailbox(mailboxId, sessionInfo);
		mailbox.setMailboxName(name); //邮箱名称
		getDatabaseService().updateRecord(mailbox);
		return mailbox;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailboxService#saveMailbox(long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void saveMailbox(long mailboxId, String name, SessionInfo sessionInfo) throws ServiceException {
		if(mailboxId>20) {
            Mailbox mailbox = loadMailbox(mailboxId, sessionInfo);
            if(mailbox==null) { //新邮箱
                mailbox = new Mailbox();
                mailbox.setId(mailboxId);
                mailbox.setUserId(sessionInfo.getUserId());
                mailbox.setMailboxName(name);
                getDatabaseService().saveRecord(mailbox);
            }
            else { //更新邮箱
                mailbox.setMailboxName(name);
                getDatabaseService().updateRecord(mailbox);
            }
        }
    }

	/**
	 * @return the webMailService
	 */
	public WebMailService getWebMailService() {
		return webMailService;
	}

	/**
	 * @param webMailService the webMailService to set
	 */
	public void setWebMailService(WebMailService webMailService) {
		this.webMailService = webMailService;
	}
}