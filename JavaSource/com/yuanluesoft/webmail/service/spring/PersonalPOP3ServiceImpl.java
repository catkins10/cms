package com.yuanluesoft.webmail.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.webmail.pojo.POP3Server;
import com.yuanluesoft.webmail.service.PersonalPOP3Service;

/**
 * 
 * @author linchuan
 *
 */
public class PersonalPOP3ServiceImpl extends BusinessServiceImpl implements PersonalPOP3Service {
	 private CryptService cryptService; //加密服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.PersonalPOP3Service#getPOP3Server(long, java.lang.String)
	 */
	public POP3Server getPOP3Server(long userId, String serverName) throws ServiceException {
		//从用户自定义的邮件服务器同步邮件
		POP3Server pop3Server = (POP3Server)getDatabaseService().findRecordByHql("from POP3Server POP3Server where POP3Server.userId=" + userId + " and POP3Server.serverAddress='" + JdbcUtils.resetQuot(serverName) + "'");
		if(pop3Server==null) {
			return null;
		}
		pop3Server.setPassword(cryptService.decrypt(pop3Server.getPassword(), "" + pop3Server.getId(), false)); //口令解密
		return pop3Server;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.PersonalPOP3Service#listPersonalPOP3Services(long)
	 */
	public List listPersonalPOP3Services(long userId) throws ServiceException {
		//从用户自定义的邮件服务器同步邮件
		List pop3Servers = getDatabaseService().findRecordsByHql("from POP3Server POP3Server where POP3Server.userId=" + userId);
		if(pop3Servers==null || pop3Servers.isEmpty()) {
			return null;
		}
		for(Iterator iterator = pop3Servers.iterator(); iterator.hasNext();) {
			POP3Server pop3Server = (POP3Server)iterator.next();
			pop3Server.setPassword(cryptService.decrypt(pop3Server.getPassword(), "" + pop3Server.getId(), false)); //口令解密
		}
		return pop3Servers;
	}

	/**
	 * @return the cryptService
	 */
	public CryptService getCryptService() {
		return cryptService;
	}

	/**
	 * @param cryptService the cryptService to set
	 */
	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}
}