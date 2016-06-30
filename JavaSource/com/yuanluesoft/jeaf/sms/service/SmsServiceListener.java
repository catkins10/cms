package com.yuanluesoft.jeaf.sms.service;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sms.pojo.SmsSend;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness;

/**
 * 短信服务侦听
 * @author linchuan
 *
 */
public interface SmsServiceListener {
	
	/**
	 * 接收到短信,如果当前侦听器处理了本信息,返回true
	 * @param senderNumber
	 * @param message
	 * @param receiveTime
	 * @param receiveNumber
	 * @param smsUnitBusiness
	 * @return
	 * @throws ServiceException
	 */
	public boolean onShortMessageReceived(String senderNumber, String message, Timestamp receiveTime, String receiveNumber, SmsUnitBusiness smsUnitBusiness) throws ServiceException;
	
	/**
	 * 短信到达用户手机
	 * @param sentMessage
	 * @throws ServiceException
	 */
	public void onShortMessageArrived(SmsSend sentMessage) throws ServiceException;
}