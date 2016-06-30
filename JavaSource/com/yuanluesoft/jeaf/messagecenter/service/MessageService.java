/*
 * Created on 2005-11-11
 *
 */

package com.yuanluesoft.jeaf.messagecenter.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.messagecenter.pojo.Message;
import com.yuanluesoft.jeaf.soap.SoapService;

/**
 * 
 * @author linchuan
 *
 */
public interface MessageService extends SoapService {
	//消息优先级
	public final static char MESSAGE_PRIORITY_LOW = '0'; //低
	public final static char MESSAGE_PRIORITY_NORMAL = '1'; //普通
	public final static char MESSAGE_PRIORITY_HIRH = '2'; //高
	
	//循环模式
	public final static String CYCLIC_MODE_NO_CYCLIC = null; //不循环
	public final static String CYCLIC_MODE_BY_MINUTE = "minute"; //按分钟
	public final static String CYCLIC_MODE_BY_HOUR = "hour"; //按小时
	public final static String CYCLIC_MODE_BY_DAY = "day"; //按天
	public final static String CYCLIC_MODE_BY_MONTH = "month"; //按月
	
	/**
	 * 发送消息
	 * @param personId 接收人
	 * @param message 消息内容
	 * @param senderId
	 * @param senderName
	 * @param priority 紧急程度
	 * @param href 链接
	 * @param sendTime
	 * @param bindSendMode 绑定发送方式
	 * @param cyclicMode 循环通知方式,空/不循环,hour/按小时,day/按天,month/按月
	 * @param cyclicTime 循环周期
	 * @param cyclicEnd 截止时间,空表示无限期
	 * @throws ServiceException
	 */
	public void sendMessageToPerson(long personId, String message, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException;
	
	/**
	 * 发送消息到部门
	 * @param orgId
	 * @param containChildOrg
	 * @param message
	 * @param senderId
	 * @param senderName
	 * @param priority
	 * @param sourceRecordId
	 * @param href
	 * @param sendTime
	 * @param bindSendMode
	 * @param cyclicMode 循环通知方式,空/不循环,hour/按小时,day/按天,month/按月
	 * @param cyclicTime 循环周期
	 * @param cyclicEnd 截止时间,空表示无限期
	 * @throws ServiceException
	 */
	public void sendMessageToOrg(long orgId, boolean containChildOrg, String message, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException;
	
	/**
	 * 发送消给指定角色
	 * @param roleId
	 * @param message
	 * @param senderId
	 * @param senderName
	 * @param priority
	 * @param href
	 * @param sendTime
	 * @param bindSendMode 绑定发送方式 
	 * @param cyclicMode 循环通知方式,空/不循环,hour/按小时,day/按天,month/按月
	 * @param cyclicTime 循环周期
	 * @param cyclicEnd 截止时间,空表示无限期
	 * @param sendToAnyone
	 * @throws ServiceException
	 */
	public void sendMessageToRole(long roleId, String message, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException;
	
	/**
	 * 发送消息给记录访问者
	 * @param visitors
	 * @param containChildOrg
	 * @param message
	 * @param senderId
	 * @param senderName
	 * @param priority
	 * @param sourceRecordId
	 * @param href
	 * @param sendTime
	 * @param bindSendMode
	 * @param cyclicMode 循环通知方式,空/不循环,hour/按小时,day/按天,month/按月
	 * @param cyclicTime 循环周期
	 * @param cyclicEnd 截止时间,空表示无限期
	 * @throws ServiceException
	 */
	public void sendMessageToVisitors(List visitors, boolean containChildOrg, String message, long senderId, String senderName, char priority, long sourceRecordId, String href, Timestamp sendTime, String bindSendMode, String cyclicMode, int cyclicTime, Timestamp cyclicEnd) throws ServiceException;
	
	/**
	 * 删除消息
	 * @param sourceRecordId
	 * @throws ServiceException
	 */
	public void removeMessages(long sourceRecordId) throws ServiceException;
	
	/**
	 * 读取消息
	 * @param messageId
	 * @param receivePersonId
	 * @throws ServiceException
	 */
	public Message retrieveMessage(long messageId, long receivePersonId) throws ServiceException;
	
	/**
	 * 消息反馈
	 * @param messageId
	 * @throws ServiceException
	 */
	public void feedbackMessage(long messageId, long receivePersonId) throws ServiceException;
	
	/**
	 * 获取发送器列表
	 */
	public List getSenders();
}