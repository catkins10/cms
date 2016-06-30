package com.yuanluesoft.telex.send.base.service;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface SendTelegramService extends BusinessService {
	
	/**
	 * 输出电报清单
	 * @param year
	 * @param month
	 * @param isCryptic
	 * @param response
	 * @throws ServiceException
	 */
	public void exportListing(int year, int month, boolean isCryptic, HttpServletResponse response) throws ServiceException;
}