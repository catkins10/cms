package com.yuanluesoft.cms.messageboard.service;

import com.yuanluesoft.cms.messageboard.pojo.MessageBoardFaq;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface MessageBoardService extends PublicService {
	
	/**
	 * 按主题获取常见问题
	 * @param subject
	 * @return
	 * @throws ServiceException
	 */
	public MessageBoardFaq findFaq(String subject) throws ServiceException;
}