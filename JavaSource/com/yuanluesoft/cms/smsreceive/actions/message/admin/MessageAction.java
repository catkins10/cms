package com.yuanluesoft.cms.smsreceive.actions.message.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class MessageAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName(com.yuanluesoft.jeaf.workflow.form.WorkflowForm)
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runMessage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //允许管理员删除
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#isMemberOfProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if("smsEditor".equals(programmingParticipantId) || "smsAuditor".equals(programmingParticipantId)) {
			com.yuanluesoft.cms.smsreceive.pojo.SmsReceiveMessage message = (com.yuanluesoft.cms.smsreceive.pojo.SmsReceiveMessage)record;
			SmsService smsService = (SmsService)getService("smsService");
			return smsService.isTransactor((message==null ? sessionInfo.getUnitId() : message.getUnitId()), (message==null ? null : message.getSmsBusinessName()), ("smsEditor".equals(programmingParticipantId) ? SmsService.SMS_RECEIVE_ACCEPTER : SmsService.SMS_RECEIVE_AUDITOR), sessionInfo);
		}
		return super.isMemberOfProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if("smsEditor".equals(programmingParticipantId) || "smsAuditor".equals(programmingParticipantId)) {
			com.yuanluesoft.cms.smsreceive.pojo.SmsReceiveMessage message = (com.yuanluesoft.cms.smsreceive.pojo.SmsReceiveMessage)record;
			SmsService smsService = (SmsService)getService("smsService");
			return smsService.listTransactors((message==null ? sessionInfo.getUnitId() : message.getUnitId()), (message==null ? null : message.getSmsBusinessName()), ("smsEditor".equals(programmingParticipantId) ? SmsService.SMS_RECEIVE_ACCEPTER : SmsService.SMS_RECEIVE_AUDITOR));
		}
		return super.listProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeApproval(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeApproval(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		super.beforeApproval(workflowForm, request, record, openMode, sessionInfo);
		workflowForm.setWorkflowApprovalOptions("同意回复,不同意回复");
	}
}