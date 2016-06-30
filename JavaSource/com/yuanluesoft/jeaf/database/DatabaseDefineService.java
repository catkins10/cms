package com.yuanluesoft.jeaf.database;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 数据库定义服务
 * @author linchuan
 *
 */
public interface DatabaseDefineService {
	
	/**
	 * 获取表列表
	 * @param applicationName
	 * @return
	 * @throws ServiceException
	 */
	public List listTables(String applicationName) throws ServiceException;

	/**
	 * 保存数据库定义
	 * @param applicationName
	 * @param tables
	 * @throws ServiceException
	 */
	public void saveDatabaseDefine(String applicationName, List tables) throws ServiceException;
	
	/**
	 * 注册应用
	 * @param applicationName
	 * @throws ServiceException
	 */
	public void registApplication(String applicationName) throws ServiceException;
}