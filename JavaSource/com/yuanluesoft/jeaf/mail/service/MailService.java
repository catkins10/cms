package com.yuanluesoft.jeaf.mail.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.mail.pojo.MailAccount;

/**
 * 
 * @author linchuan
 *
 */
public interface MailService extends BusinessService {

	/**
	 * 发送邮件
	 * @param senderId
	 * @param senderName
	 * @param senderUnitId
	 * @param receivers
	 * @param subject
	 * @param body
	 * @param dispositionNotification
	 * @throws ServiceException
	 */
	public void sendMail(long senderId, String senderName, long senderUnitId, String receivers, String subject, String body, boolean dispositionNotification) throws ServiceException;
	
	/**
	 * 获取邮件帐号
	 * @param orgId
	 * @return
	 * @throws ServiceException
	 */
	public MailAccount getMailAccount(long orgId) throws ServiceException;
}
