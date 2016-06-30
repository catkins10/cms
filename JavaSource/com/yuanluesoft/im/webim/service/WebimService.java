package com.yuanluesoft.im.webim.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface WebimService {
	
	/**
	 * 处理接收到的消息
	 * @param currentUserId
	 * @param message
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void processMessage(long currentUserId, Message message, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}