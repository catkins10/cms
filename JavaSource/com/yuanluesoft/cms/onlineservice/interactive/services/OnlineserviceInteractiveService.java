package com.yuanluesoft.cms.onlineservice.interactive.services;

import java.util.List;

import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface OnlineserviceInteractiveService extends PublicService {
	
	/**
	 * 获取近期投诉列表
	 * @param itemId
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listRecentCompaints(long itemId, int limit) throws ServiceException;
	
	/**
	 * 获取近期咨询列表
	 * @param itemId
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listRecentConsults(long itemId, int limit) throws ServiceException;
}