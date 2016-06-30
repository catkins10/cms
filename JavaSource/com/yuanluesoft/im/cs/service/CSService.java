package com.yuanluesoft.im.cs.service;

import java.util.List;

import com.yuanluesoft.im.cs.pojo.CSParameter;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface CSService extends BusinessService {

	/**
	 * 获取客服参数
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public CSParameter loadParameter(long siteId) throws ServiceException;
	
	/**
	 * 获取常用答复列表
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listOftenUsedReplies(long siteId) throws ServiceException;
}