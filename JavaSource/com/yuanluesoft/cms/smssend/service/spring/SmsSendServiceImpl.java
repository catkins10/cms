package com.yuanluesoft.cms.smssend.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.smssend.pojo.SmsSendMessage;
import com.yuanluesoft.cms.smssend.pojo.SmsSendWorkflow;
import com.yuanluesoft.cms.smssend.service.SmsSendService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author linchuan
 *
 */
public class SmsSendServiceImpl extends BusinessServiceImpl implements SmsSendService {
	private SmsService smsService; //短信服务
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private RecordControlService recordControlService; //记录控制服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long workflowConfigId = RequestUtils.getParameterLongValue(notifyRequest, "workflowConfigId"); //工作流配置ID
		SmsSendWorkflow workflowConfig = (SmsSendWorkflow)load(SmsSendWorkflow.class, workflowConfigId);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程删除操作
			workflowConfig.setWorkflowId(null); //流程ID 
			workflowConfig.setWorkflowName(null); //流程名称
		}
		else { //新建或更新流程
			workflowConfig.setWorkflowId(workflowId); //流程ID 
			workflowConfig.setWorkflowName(workflowPackage.getName()); //流程名称
		}
		update(workflowConfig);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssend.service.SmsSendService#sendSmsMessage(com.yuanluesoft.cms.smssend.pojo.SmsSendMessage, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void sendSmsMessage(SmsSendMessage smsSendMessage, SessionInfo sessionInfo) throws ServiceException {
		if(smsSendMessage.getSendTime()!=null) {
			return;
		}
		List receivers = recordControlService.listVisitors(smsSendMessage.getId(), SmsSendMessage.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(receivers==null || receivers.isEmpty()) {
			return;
		}
		smsService.sendShortMessage(sessionInfo.getUserId(), sessionInfo.getUserName(), smsSendMessage.getUnitId(), smsSendMessage.getSmsBusinessName(), ListUtils.join(receivers, "visitorId", ",", false), null, smsSendMessage.getContent(), null, smsSendMessage.getId(), true, null, true);
		smsSendMessage.setSendCount(receivers.size()); //发送条数
		smsSendMessage.setSendTime(DateTimeUtils.now()); //发送时间
		update(smsSendMessage);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smssend.service.SmsSendService#getSmsSendWorkflowEntry(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public WorkflowEntry getSmsSendWorkflowEntry(SessionInfo sessionInfo) throws ServiceException {
		//获取流程ID
		String hql = "select SmsSendWorkflow.workflowId" +
					 " from SmsSendWorkflow SmsSendWorkflow, OrgSubjection OrgSubjection" +
					 " where OrgSubjection.directoryId=" + sessionInfo.getUnitId() +
					 " and SmsSendWorkflow.orgId=OrgSubjection.parentDirectoryId" +
					 " and not SmsSendWorkflow.workflowId is null" +
					 " order by OrgSubjection.id";
		String workflowId = (String)getDatabaseService().findRecordByHql(hql);
		if(workflowId==null) {
			return null;
		}
		//办理人回调
		WorkflowParticipantCallback participantCallback = new WorkflowParticipantCallback() {
			public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				SmsSendMessage message = (SmsSendMessage)workflowData;
				return getSmsService().isTransactor((message==null ? sessionInfo.getUnitId() : message.getUnitId()), (message==null ? null : message.getSmsBusinessName()), ("smsEditor".equals(programmingParticipantId) ? SmsService.SMS_SEND_EDITOR : SmsService.SMS_SEND_AUDITOR), sessionInfo);
			}

			public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				SmsSendMessage message = (SmsSendMessage)workflowData;
				return getSmsService().listTransactors((message==null ? sessionInfo.getUnitId() : message.getUnitId()), (message==null ? null : message.getSmsBusinessName()), ("smsEditor".equals(programmingParticipantId) ? SmsService.SMS_SEND_EDITOR : SmsService.SMS_SEND_AUDITOR));
			}

			public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				return participants;
			}
		};
		return workflowExploitService.getWorkflowEntry(workflowId, participantCallback, null, sessionInfo);
	}

	/**
	 * @return the smsService
	 */
	public SmsService getSmsService() {
		return smsService;
	}

	/**
	 * @param smsService the smsService to set
	 */
	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}

	/**
	 * @return the recordControlService
	 */
	public RecordControlService getRecordControlService() {
		return recordControlService;
	}

	/**
	 * @param recordControlService the recordControlService to set
	 */
	public void setRecordControlService(RecordControlService recordControlService) {
		this.recordControlService = recordControlService;
	}
}