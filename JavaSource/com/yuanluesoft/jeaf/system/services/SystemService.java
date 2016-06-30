package com.yuanluesoft.jeaf.system.services;

import java.sql.Date;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface SystemService {
	
	/**
	 * 生成注册码
	 * @return
	 * @throws ServiceException
	 */
	public String generateRegistCode() throws ServiceException;
	
	/**
	 * 注册系统,返回使用的截止时间,空表示没有限制
	 * @param sn
	 * @return
	 * @throws ServiceException
	 */
	public Date registSystem(String sn) throws ServiceException;
	
	/**
	 * 检查系统是否可用
	 * @return
	 * @throws ServiceException
	 */
	public boolean isSystemUseful() throws ServiceException;
	
	/**
	 * 初始化系统
	 * @param systemName
	 * @param managerName
	 * @param managerLoginName
	 * @param managerPassword
	 * @throws ServiceException
	 */
	public void initSystem(String systemName, String managerName, String managerLoginName, String managerPassword) throws ServiceException;
}