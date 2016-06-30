package com.yuanluesoft.jeaf.messagecenter.sender.mail;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.mail.service.MailService;
import com.yuanluesoft.jeaf.messagecenter.pojo.Message;
import com.yuanluesoft.jeaf.messagecenter.sender.SendException;
import com.yuanluesoft.jeaf.messagecenter.sender.Sender;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 邮件通知
 * @author linchuan
 *
 */
public class MailSender extends Sender {
	private DatabaseService databaseService; //数据库服务
	private MailService mailService; //邮件服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#sendMessage(com.yuanluesoft.jeaf.messagecenter.pojo.Message, int)
	 */
	public boolean sendMessage(Message message, int feedbackDelay) throws SendException {
		String hql = "select Person.mailAddress" +
					 " from Person Person" +
					 " where Person.id=" + message.getReceivePersonId();
		String mailAddress = (String)databaseService.findRecordByHql(hql);
		if(mailAddress==null || mailAddress.isEmpty()) {
			return false;
		}
		try {
			mailService.sendMail(0, null, 0, mailAddress, StringUtils.slice(message.getContent(), 100, "..."), message.getContent(), false);
		} 
		catch(ServiceException e) {
			Logger.exception(e);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#loadPersonalCustom(long)
	 */
	public Object loadPersonalCustom(long personId) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#savePersonalCustom(long, java.lang.Object)
	 */
	public void savePersonalCustom(long personId, Object config) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#stopSender()
	 */
	public void stopSender() throws SendException {
		
	}

	/**
	 * @return the mailService
	 */
	public MailService getMailService() {
		return mailService;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}