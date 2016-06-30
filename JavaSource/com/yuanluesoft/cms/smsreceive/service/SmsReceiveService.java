package com.yuanluesoft.cms.smsreceive.service;

import com.yuanluesoft.cms.smsreceive.pojo.SmsReceiveMessage;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.service.SmsServiceListener;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 短信接收服务
 * @author linchuan
 *
 */
public interface SmsReceiveService extends BusinessService, WorkflowConfigureCallback, SmsServiceListener {
	
	/**
	 * 回复短信
	 * @param smsReceiveMessage
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void replySmsMessage(SmsReceiveMessage smsReceiveMessage, SessionInfo sessionInfo) throws ServiceException;
}