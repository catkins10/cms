/*
 * Created on 2006-5-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.service;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.webmail.pojo.Mailbox;

/**
 *
 * @author linchuan
 *
 */
public interface MailboxService extends ViewService {
    public final static int MAILBOX_TMP_ID = 0; //临时邮箱,用于存放需要发送的邮件,定时清除
    public final static int MAILBOX_OUTBOX_ID = 1; //发件箱
    public final static int MAILBOX_DRAFT_ID = 2; //草稿箱
    public final static int MAILBOX_INBOX_ID = 10; //收件箱
    public final static int MAILBOX_RECYCLE_ID = 11; //回收站
    public final static int MAILBOX_TRASH_ID = 12; //垃圾邮件

	/**
	 * 获取邮箱列表
	 * @param inboxOnly 仅收件用邮箱
	 * @param detail 包含明细(邮件数量,占用空间等)
	 * @return
	 * @throws ServiceException
	 */
	public List listMailboxes(boolean inboxOnly, boolean detail, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 加载邮箱
	 * @param mailSession
	 * @param mailboxId
	 * @return
	 * @throws ServiceException
	 */
	public Mailbox loadMailbox(long mailboxId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 添加邮箱
	 * @param name
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Mailbox addMailbox(String name, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 邮箱重命名
	 * @param mailboxId
	 * @param name
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Mailbox renameMailbox(long mailboxId, String name, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 删除邮箱
	 * @param mailSession
	 * @param mailboxId
	 * @throws ServiceException
	 */
	public void deleteMailbox(long mailboxId, SessionInfo sessionInfo) throws ServiceException;
}