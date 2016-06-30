package com.yuanluesoft.jeaf.point.service;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;


/**
 * 积分服务,由各个应用来实现
 * @author linchuan
 *
 */
public interface PointService {

	/**
	 * 根据积分项目增加积分,返回增加的积分数量
	 * @param pointItems
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public int addPoint(List pointItems, long personId) throws ServiceException;
	
	/**
	 * 为单个积分项目增加积分,返回增加的积分数量
	 * @param itemName
	 * @param itemCount
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public int addPoint(String itemName, int itemCount, long personId) throws ServiceException;
	
	/**
	 * 获取用户总积分
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public int getPoint(long personId) throws ServiceException;
}