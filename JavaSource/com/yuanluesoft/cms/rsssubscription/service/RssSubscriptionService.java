package com.yuanluesoft.cms.rsssubscription.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * RSS订阅服务
 * @author linchuan
 *
 */
public interface RssSubscriptionService {
	
	/**
	 *  输出RSS频道
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void writeRssChannel(HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}