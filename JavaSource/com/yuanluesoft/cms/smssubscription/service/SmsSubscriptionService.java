package com.yuanluesoft.cms.smssubscription.service;

import java.util.List;

import com.yuanluesoft.cms.smssubscription.pojo.SmsSubscription;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sms.service.SmsServiceListener;

/**
 * 短信订阅服务
 * @author linchuan
 *
 */
public interface SmsSubscriptionService extends SmsServiceListener, BusinessService {
	
	/**
	 * 获取内容定义列表
	 * @return
	 * @throws ServiceException
	 */
	public List listContentDefinitions() throws ServiceException;
	
	/**
	 * 校验规则
	 * @param subscribePrefixRule
	 * @param unsubscribeRule
	 * @param smsServiceId
	 * @return
	 * @throws ServiceException
	 */
	public void valideteRule(String subscribePrefixRule, String unsubscribeRule, long smsServiceId) throws ServiceException;
	
	/**
	 * 根据订阅参数获取订阅内容描述,如果订阅参数为空,则返回contentName
	 * @param contentName 订阅内容
	 * @param subscribeParameter 订阅参数
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public String getContentDescription(String contentName, String subscribeParameter, long siteId) throws ServiceException;
	
	/**
	 * 短信订阅
	 * @param subscriberNumber
	 * @param contentName
	 * @param subscribeParameter
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public SmsSubscription subscribe(String subscriberNumber, String contentName, String subscribeParameter, long siteId) throws ServiceException;

	/**
	 * 短信退订
	 * @param subscriberNumber
	 * @param contentName
	 * @param subscribeParameter
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public SmsSubscription unsubscribe(String subscriberNumber, String contentName, String subscribeParameter, long siteId) throws ServiceException;

	/**
	 * 给订阅者发送消息
	 * @param contentServiceName
	 * @param contentName
	 * @param siteId
	 * @param message
	 * @param smsContentCallback
	 * @throws ServiceException
	 */
	public void sendMessageToSubscriber(String contentServiceName, String contentName, long siteId, String message, SmsContentCallback smsContentCallback) throws ServiceException;
}