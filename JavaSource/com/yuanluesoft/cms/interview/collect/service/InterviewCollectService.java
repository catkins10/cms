package com.yuanluesoft.cms.interview.collect.service;

import com.yuanluesoft.cms.interview.collect.pojo.InterviewCollectPrologue;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 在线访谈主题征集服务
 * @author linchuan
 *
 */  
public interface InterviewCollectService extends PublicService {
	
	/**
	 * 获取征集说明
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public InterviewCollectPrologue getCollectPrologue(long siteId) throws ServiceException;
}