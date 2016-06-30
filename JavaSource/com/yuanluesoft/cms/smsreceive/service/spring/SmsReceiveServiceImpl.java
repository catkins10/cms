package com.yuanluesoft.cms.smsreceive.service.spring;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.smsreceive.pojo.SmsReceiveMessage;
import com.yuanluesoft.cms.smsreceive.pojo.SmsReceiveWorkflow;
import com.yuanluesoft.cms.smsreceive.service.SmsReceiveService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sms.pojo.SmsSend;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 * 
 * @author linchuan
 *
 */
public class SmsReceiveServiceImpl extends BusinessServiceImpl implements SmsReceiveService {
	private SmsService smsService; //短信服务
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private SessionService sessionService; //会话服务
	private RecordControlService recordControlService; //记录控制服务
	private String smsTypes; //短信类型,如:党务、村务公开,农政通
	
	/**
	 * 初始化
	 *
	 */
	public void init() {
		if(smsTypes==null || smsTypes.isEmpty()) {
			return;
		}
		String[] types = smsTypes.split(",");
		for(int i=0; i<types.length; i++) {
			smsService.registSmsBusiness(types[i], true, true);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.smsreceive.service.SmsReceiveService#replySmsMessage(com.yuanluesoft.cms.smsreceive.pojo.SmsReceiveMessage, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void replySmsMessage(SmsReceiveMessage smsReceiveMessage, SessionInfo sessionInfo) throws ServiceException {
		if(smsReceiveMessage.getReplyContent()==null || smsReceiveMessage.getReplyContent().isEmpty()) {
			return;
		}
		//发送短信
		smsService.sendShortMessage(sessionInfo.getUserId(), sessionInfo.getUserName(), smsReceiveMessage.getUnitId(), smsReceiveMessage.getSmsBusinessName(), null, smsReceiveMessage.getSenderNumber(), smsReceiveMessage.getReplyContent(), null, -1, false, null, true);
		//更新记录
		smsReceiveMessage.setReplyTime(DateTimeUtils.now());
		update(smsReceiveMessage);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsServiceListener#onShortMessageArrived(com.yuanluesoft.jeaf.sms.pojo.SmsSend)
	 */
	public void onShortMessageArrived(SmsSend sentMessage) throws ServiceException {
		//不需要处理短信到达通知
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.sms.service.SmsServiceListener#onShortMessageReceived(java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.String, com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness)
	 */
	public boolean onShortMessageReceived(String senderNumber, String message, Timestamp receiveTime, String receiveNumber, SmsUnitBusiness smsUnitBusiness) throws ServiceException {
		if(smsUnitBusiness==null || ("," + smsTypes + ",").indexOf("," + smsUnitBusiness.getBusinessName() + ",")==-1) { //没有和短信业务挂钩,获取短信业务不在短信类型中
			return false;
		}
		//获取流程ID
		String hql = "select SmsReceiveWorkflow.workflowId" +
					 " from SmsReceiveWorkflow SmsReceiveWorkflow, OrgSubjection OrgSubjection" +
					 " where OrgSubjection.directoryId=" + smsUnitBusiness.getUnitConfig().getUnitId() +
					 " and SmsReceiveWorkflow.orgId=OrgSubjection.parentDirectoryId" +
					 " and not SmsReceiveWorkflow.workflowId is null" +
					 " order by OrgSubjection.id";
		String workflowId = (String)getDatabaseService().findRecordByHql(hql);
		if(workflowId==null) {
			return false;
		}
		//创建短信接收记录
		final SmsReceiveMessage smsReceiveMessage = new SmsReceiveMessage();
		smsReceiveMessage.setId(UUIDLongGenerator.generateId()); //ID
		smsReceiveMessage.setContent(message); //短信内容
		smsReceiveMessage.setSenderNumber(senderNumber); //发送人号码
		smsReceiveMessage.setReceiverNumber(receiveNumber); //接收号码
		smsReceiveMessage.setReceiveTime(receiveTime); //接收时间
		smsReceiveMessage.setUnitId(smsUnitBusiness.getUnitConfig().getUnitId()); //受理单位ID
		smsReceiveMessage.setUnitName(smsUnitBusiness.getUnitConfig().getUnitName()); //受理单位名称
		smsReceiveMessage.setSmsBusinessName(smsUnitBusiness.getBusinessName()); //短信业务分类
		
		//办理人回调
		WorkflowParticipantCallback participantCallback = new WorkflowParticipantCallback() {
			public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				return getSmsService().isTransactor(smsReceiveMessage.getUnitId(), smsReceiveMessage.getSmsBusinessName(), ("smsEditor".equals(programmingParticipantId) ? SmsService.SMS_RECEIVE_ACCEPTER : SmsService.SMS_RECEIVE_AUDITOR), sessionInfo);
			}

			public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				return getSmsService().listTransactors(smsReceiveMessage.getUnitId(), smsReceiveMessage.getSmsBusinessName(), ("smsEditor".equals(programmingParticipantId) ? SmsService.SMS_RECEIVE_ACCEPTER : SmsService.SMS_RECEIVE_AUDITOR));
			}

			public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				return participants;
			}
		};
		
		//获取流程入口
		SessionInfo sessionInfo;
		try {
			sessionInfo = sessionService.getSessionInfo(SessionService.ANONYMOUS);
		} 
		catch (SessionException e) {
			throw new ServiceException(e.getMessage());
		}
		sessionInfo.setUserName(senderNumber);
	
		WorkflowEntry workflowEntry = workflowExploitService.getWorkflowEntry(workflowId, participantCallback, smsReceiveMessage, sessionInfo);
		if(workflowEntry==null) {
			throw new ServiceException("流程未定义或配置错误，接收失败。");
		}
		
		//创建流程实例并自动发送
		WorkflowMessage workflowMessage = new WorkflowMessage("短信接收", "接收到新短信:" + message, null);
		Element activity = (Element)workflowEntry.getActivityEntries().get(0);
		String workflowInstanceId = workflowExploitService.createWorkflowInstanceAndSend(workflowEntry.getWorkflowId(), activity.getId(), smsReceiveMessage, workflowMessage, participantCallback, sessionInfo);
		smsReceiveMessage.setWorkflowInstanceId(workflowInstanceId); //流程实例ID
		//保存主记录
		save(smsReceiveMessage);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long workflowConfigId = RequestUtils.getParameterLongValue(notifyRequest, "workflowConfigId"); //工作流配置ID
		SmsReceiveWorkflow workflowConfig = (SmsReceiveWorkflow)load(SmsReceiveWorkflow.class, workflowConfigId);
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
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
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

	/**
	 * @return the smsTypes
	 */
	public String getSmsTypes() {
		return smsTypes;
	}

	/**
	 * @param smsTypes the smsTypes to set
	 */
	public void setSmsTypes(String smsTypes) {
		this.smsTypes = smsTypes;
	}
}