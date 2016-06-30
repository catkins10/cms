package com.yuanluesoft.jeaf.system.services;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 初始化服务,由需要初始化的应用实现
 * @author linchuan
 *
 */
public interface InitializeService {
	
	/**
	 * 初始化
	 * @param systemName
	 * @param managerId
	 * @param managerName
	 * @return
	 * @throws ServiceException
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException;
}