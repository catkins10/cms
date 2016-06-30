package com.yuanluesoft.webmail.service.mailservice.qmail;

import java.io.File;
import java.util.List;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.webmail.model.MailSession;
import com.yuanluesoft.webmail.pojo.Mail;
import com.yuanluesoft.webmail.pojo.qmail.VpopMail;
import com.yuanluesoft.webmail.service.mailservice.MailReadCallback;
import com.yuanluesoft.webmail.service.mailservice.MailService;
import com.yuanluesoft.webmail.service.mailservice.MailWriteCallback;

/**
 * 
 * @author linchuan
 *
 */
public class MailServiceImpl implements MailService {
	private DatabaseService databaseService; //数据库访问

	/**
	 * 获取邮箱目录
	 * @param mailAccount
	 * @param mailbox
	 * @return
	 * @throws ServiceException
	 */
	protected String getMailboxDirectory(MailSession mailSession, String mailbox) throws ServiceException {
		VpopMail vpopMail= (VpopMail)databaseService.findRecordByHql("from VpopMail VpopMail where VpopMail.name='" + JdbcUtils.resetQuot(mailSession.getLoginName()) + "' and VpopMail.password='" + JdbcUtils.resetQuot(mailSession.getPassword()) + "'");
		if(vpopMail==null) {
			throw new ServiceException("login failed");
		}
		//mailSession.setMailAddress(mailUserName + "@" + vpopMail.getDomain());
		String mailboxDirectory = vpopMail.getDirectory() + "/Maildir/";
		if(!mailbox.equals("inbox")) {
			mailboxDirectory += mailbox + "/";
			File dir = new File(mailboxDirectory);
			if(!dir.exists()) {
				dir.mkdir();
			}
			dir = new File(mailboxDirectory + "new");
			if(!dir.exists()) {
				dir.mkdir();
			}
			dir = new File(mailboxDirectory + "cur");
			if(!dir.exists()) {
				dir.mkdir();
			}
		}
		return mailboxDirectory;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#writeMail(com.yuanluesoft.webmail.model.MailSession, com.yuanluesoft.webmail.pojo.Mail, com.yuanluesoft.webmail.service.mailservice.MailWriteCallback)
	 */
	public void writeMail(MailSession mailSession, Mail mail, MailWriteCallback mailWriteCallback) throws ServiceException {
		/*mail.setDate(new Timestamp(System.currentTimeMillis()));
		Process process;
		try {
			process = Runtime.getRuntime().exec("/usr/sbin/sendmail -t");
		} catch (IOException e) {
			throw new ServiceException();
		}
		
		//outputMail(mailSession, mail, process.getOutputStream());*/
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#changePassword(com.yuanluesoft.webmail.model.MailSession, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void changePassword(MailSession mailManageSession, String loginName, String mailDomain, String password) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#createMailAccount(com.yuanluesoft.webmail.model.MailSession, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String createMailAccount(MailSession mailManageSession, String loginName, String mailDomain, String password) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#deleteMail(com.yuanluesoft.webmail.model.MailSession, java.lang.String)
	 */
	public void deleteMail(MailSession mailReceiveSession, String mailId) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#listMails(com.yuanluesoft.webmail.model.MailSession)
	 */
	public List listMails(MailSession mailReceiveSession) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#login(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, int)
	 */
	public MailSession login(String mailAddress, String serverHost, int servicePort, String loginName, String password, int sessionType) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#logout(com.yuanluesoft.webmail.model.MailSession)
	 */
	public void logout(MailSession mailSession) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#readMail(com.yuanluesoft.webmail.model.MailSession, java.lang.String, com.yuanluesoft.webmail.service.mailservice.MailReadCallback)
	 */
	public void readMail(MailSession mailReceiveSession, String mailId, MailReadCallback mailReadCallback) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#removeMailAccount(com.yuanluesoft.webmail.model.MailSession, java.lang.String, java.lang.String)
	 */
	public void removeMailAccount(MailSession mailManageSession, String loginName, String mailDomain) throws ServiceException {
		
	}
}
