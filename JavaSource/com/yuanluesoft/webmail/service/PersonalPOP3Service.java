package com.yuanluesoft.webmail.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.webmail.pojo.POP3Server;

/**
 * 个人定义的pop3服务管理
 * @author linchuan
 *
 */
public interface PersonalPOP3Service extends BusinessService {

	/**
	 * 获取个人定义的pop3服务列表
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public List listPersonalPOP3Services(long userId) throws ServiceException;
	
	/**
	 * 获取pop3服务信息
	 * @param userId
	 * @param serverName
	 * @return
	 * @throws ServiceException
	 */
	public POP3Server getPOP3Server(long userId, String serverName) throws ServiceException;
}
