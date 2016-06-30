package com.yuanluesoft.cms.advice.service;

import com.yuanluesoft.cms.advice.pojo.AdviceFeedback;
import com.yuanluesoft.cms.advice.pojo.AdviceTopic;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface AdviceService extends BusinessService {

	/**
	 * 获取征集主题
	 * @param topicId
	 * @return
	 * @throws ServiceException
	 */
	public AdviceTopic getAdviceTopic(long topicId) throws ServiceException;
	
	/**
	 * 检查是否有结果反馈
	 * @param topicId
	 * @return
	 * @throws ServiceException
	 */
	public boolean hasFeedback(long topicId) throws ServiceException;
	
	/**
	 * 检查是否有已发布的意见
	 * @param topicId
	 * @return
	 * @throws ServiceException
	 */
	public boolean hasPublicedAdvice(long topicId) throws ServiceException;
	
	/**
	 * 按主题ID获取结果反馈
	 * @param topicId
	 * @return
	 * @throws ServiceException
	 */
	public AdviceFeedback getAdviceFeedback(long topicId) throws ServiceException;
}