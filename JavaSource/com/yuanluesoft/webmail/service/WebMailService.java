/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.webmail.pojo.Mail;

/**
 * 
 * @author root
 * 
 */
public interface WebMailService extends BusinessService {
     
	/**
	 * 同步更新邮件列表
	 * @param readLevel 邮件读取级别,详见MailReader
	 * @param sessionInfo
	 */
	public void synchMailList(char readLevel, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 创建新邮件
	 * @param mailSession
	 * @return
	 * @throws ServiceException
	 */
	public Mail createMail(String mailTo, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 答复邮件
	 * @param mailSession
	 * @param mailIds
	 * @return
	 * @throws ServiceException
	 */
	public Mail replyMail(String mailIds, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 转发邮件
	 * @param mailSession
	 * @param mailId
	 * @return
	 * @throws ServiceException
	 */
	public Mail forwardMail(long mailId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 接收邮件
	 * @param mailSession
	 * @param mailId
	 * @return
	 * @throws ServiceException
	 */
	public Mail receiveMail(long mailId, boolean setReadFlag, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 发邮件
	 * @throws ServiceException
	 */
	public void sendMail(Mail mail, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 保存或更新邮件
	 * @param mail
	 * @param sessionInfo
	 * @param isNew
	 * @throws ServiceException
	 */
	public void saveMail(Mail mail, SessionInfo sessionInfo, boolean isNew) throws ServiceException;
	
	/**
	 * 删除邮件
	 * @param mailSession
	 * @param mailId
	 * @throws ServiceException
	 */
	public void deleteMail(long mailId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 删除临时邮件
	 * @throws ServiceException
	 */
	public void cleanTemporaryMail() throws ServiceException;
	
	/**
	 * 移动邮件
	 * @param mailSession
	 * @param mail
	 * @param mailbox
	 * @throws ServiceException
	 */
	public void moveMail(long mailId, long mailboxId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 下载在邮件服务器上的附件
	 * @param request
	 * @param response
	 * @param mailAttachmentId
	 * @param inline
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void downloadAttachmentOnMailServer(HttpServletRequest request, HttpServletResponse response, long mailAttachmentId, boolean inline, SessionInfo sessionInfo) throws ServiceException;
}