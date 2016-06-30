package com.yuanluesoft.cms.smssend.service;

import com.yuanluesoft.cms.smssend.pojo.SmsSendMessage;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 短信发送服务
 * @author linchuan
 *
 */
public interface SmsSendService extends BusinessService, WorkflowConfigureCallback {
	
	/**
	 * 发送短信
	 * @param smsSendMessage
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void sendSmsMessage(SmsSendMessage smsSendMessage, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取短信发送流程入口
	 * @param sessionInfo
	 * @return
	 */
	public WorkflowEntry getSmsSendWorkflowEntry(SessionInfo sessionInfo) throws ServiceException;
}