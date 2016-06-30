package com.yuanluesoft.jeaf.sms.service;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.model.SmsBusiness;

/**
 * 短信服务,实现收发
 * @author linchuan
 *
 */
public interface SmsService extends BusinessService {
	public final static char SMS_SEND_EDITOR = '0'; //短信发送编辑
	public final static char SMS_SEND_AUDITOR = '1'; //短信发送审核
	public final static char SMS_RECEIVE_ACCEPTER = '2'; //短信接收受理
	public final static char SMS_RECEIVE_AUDITOR = '3'; //短信接收审核
	
	/**
	 * 发送短信
	 * @param senderId
	 * @param senderName
	 * @param senderUnitId 发送人所在单位
	 * @param smsBusinessName 短信业务名称,如:短信订阅,系统消息
	 * @param receiverNumbers 电话号码为空时,从用户记录中获取
	 * @param message
	 * @param sendTime
	 * @param sourceRecordId 源记录ID,可以用来查询短信发送情况
	 * @param checkSent 按sourceRecordId检查是否已经发送过
	 * @param remark 备注,应用程序可以根据需要设置自定义的内容
	 * @param arriveCheck 是否检查短信有没有到用户手机
	 * @param receiverNames
	 * @throws ServiceException
	 */
	public void sendShortMessage(long senderId, String senderName, long senderUnitId, String smsBusinessName, String receiverIds, String receiverNumbers, String message, Timestamp sendTime, long sourceRecordId, boolean checkSent, String remark, boolean arriveCheck) throws ServiceException;
	
	/**
	 * 按单位ID和业务名称获取短信号码
	 * @param unitId
	 * @param smsBusinessName
	 * @return
	 * @throws ServiceException
	 */
	public String getSmsNumber(long unitId, String smsBusinessName) throws ServiceException;
	
	/**
	 * 获取较少使用的客户端名称
	 * @return
	 * @throws ServiceException
	 */
	public String getLessUseClientName() throws ServiceException;
	
	/**
	 * 判断是否已经发送过短信
	 * @param unitId
	 * @param businessName
	 * @return
	 * @throws ServiceException
	 */
	public boolean isSmsSent(long unitId, String businessName) throws ServiceException;
	
	/**
	 * 判断用户是否经办人
	 * @param unitId
	 * @param businessName
	 * @param transactorType SMS_SEND_EDITOR/SMS_SEND_AUDITOR/SMS_RECEIVE_ACCEPTER/SMS_RECEIVE_AUDITOR
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean isTransactor(long unitId, String businessName, char transactorType, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取经办人，返回SmsUnitBusinessPrivilege列表
	 * @param unitId
	 * @param businessName
	 * @param transactorType
	 * @return
	 * @throws ServiceException
	 */
	public List listTransactors(long unitId, String businessName, char transactorType) throws ServiceException;
	
	/**
	 * 获取短信客户端列表
	 * @return
	 */
	public List getSmsClients();
	
	/**
	 * 注册短信业务
	 * @param name
	 * @param sendPopedomConfig
	 * @param receivePopedomConfig
	 */
	public void registSmsBusiness(String name, boolean sendPopedomConfig, boolean receivePopedomConfig);
	
	/**
	 * 获取短信业务
	 * @param name
	 * @return
	 */
	public SmsBusiness getSmsBusiness(String name);
}