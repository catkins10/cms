package com.yuanluesoft.jeaf.stat.service;

import java.util.List;

import com.yuanluesoft.jeaf.stat.model.Statistics;
import com.yuanluesoft.jeaf.stat.pojo.LoginStat;
import com.yuanluesoft.jeaf.exception.ServiceException;


/**
 * 
 * @author linchuan
 *
 */
public interface StatService {
	
	/**
	 * 用户访问,返回新的访问次数
	 * @param applicationName
	 * @param pageName
	 * @param recordId
	 * @return
	 * @throws ServiceException
	 */
	public long access(String applicationName, String pageName, long recordId) throws ServiceException;
	
	/**
	 * 获取访问次数
	 * @param applicationName
	 * @param pageName
	 * @param recordId
	 * @return
	 * @throws ServiceException
	 */
	public long getAccessTimes(String applicationName, String pageName, long recordId) throws ServiceException;
	
	/**
	 * 记录非匿名用户的访问历史
	 * @param applicationName
	 * @param pageName
	 * @param recordId
	 * @param userId
	 * @param userName
	 * @throws ServiceException
	 */
	public void access(String applicationName, String pageName, long recordId, long userId, String userName) throws ServiceException;

	/**
	 * 获取记录的访问用户列表
	 * @param applicationName
	 * @param pageName
	 * @param recordId
	 * @return
	 * @throws ServiceException
	 */
	public List listAccessUsers(String applicationName, String pageName, long recordId) throws ServiceException;
	
	/**
	 * 用户登录
	 * @param personId
	 * @param personName
	 * @param personType
	 * @throws ServiceException
	 */
	public void login(long personId, String personName, int personType) throws ServiceException;
	
	/**
	 * 获取登录情况
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public LoginStat getLoginStat(long personId) throws ServiceException;
	
	/**
	 * 清除当天的登录记录,每天0:0执行
	 * @throws ServiceException
	 */
	public void clearTodayLoginStat() throws ServiceException;
	
	/**
	 * 获取统计报告
	 * @return
	 * @throws ServiceException
	 */
	public Statistics getStatistics() throws ServiceException;
}