package com.yuanluesoft.cms.infopublic.request.service;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface PublicRequestService {
	
	/**
	 * 统计,返回Stat模型列表,其中第一个为合计
	 * @param beginDate
	 * @param endDate
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List stat(Date beginDate, Date endDate, long siteId) throws ServiceException;
}