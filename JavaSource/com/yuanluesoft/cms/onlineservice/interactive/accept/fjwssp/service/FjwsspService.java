package com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.service;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface FjwsspService {

	/**
	 * 获取办件列表
	 * @return
	 * @throws ServiceException
	 */
	public List listCases() throws ServiceException;
	
	/**
	 * 更新办件列表,供定时器调用
	 *
	 */
	public void updateCases();
}