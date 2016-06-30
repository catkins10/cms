package com.yuanluesoft.cms.smssubscription.service;

import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 短信内容服务,由内容提供者实现
 * @author linchuan
 *
 */
public interface SmsContentService {
	//消息发送方式
	public static final String SEND_MODE_REPLY = "reply"; //实时回复,如：根据受理编号查询办理情况
	public static final String SEND_MODE_NEWS = "news"; //有新消息时发送,如：政府信息,发布以后自动发送给订购人
	
	/**
	 * 获取支持的短信内容定义列表
	 * @return
	 * @throws ServiceException
	 */
	public List listSmsContentDefinitions() throws ServiceException;
	
	/**
	 * 获取回复短信的内容,当消息发送方式为实时回复(SEND_MODE_REPLY)时实现
	 * @param contentName 内容名称
	 * @param fieldValueMap 字段对应的值
	 * @param message 用户发送的信息名称
	 * @param senderNumber 发送人号码
	 * @param siteId 站点ID
	 * @return
	 * @throws ServiceException
	 */
	public String getSmsReplyContent(String contentName, Map fieldValueMap, String message, String senderNumber, long siteId) throws ServiceException;
	
	/**
	 * 根据订阅参数获取订阅内容描述
	 * @param contentName
	 * @param subscribeParameter
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public String getContentDescription(String contentName, String subscribeParameter, long siteId) throws ServiceException;
}