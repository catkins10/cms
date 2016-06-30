package com.yuanluesoft.cms.smssend.actions.message.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.smssend.forms.admin.SmsSendMessage;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.service.SmsService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
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
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.cms.smssend.pojo.SmsSendMessage message = (com.yuanluesoft.cms.smssend.pojo.SmsSendMessage)record;
		if(message==null || message.getSendTime()==null) {
			form.getFormActions().removeFormAction("发送明细");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		SmsSendMessage messageForm = (SmsSendMessage)form;
		messageForm.setCreator(sessionInfo.getUserName()); //创建者
		messageForm.setUnitName(sessionInfo.getUnitName()); //创建者所在单位名称
		messageForm.setCreated(DateTimeUtils.now()); //创建时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		SmsSendMessage messageForm = (SmsSendMessage)form;
		//设置接收人列表
		messageForm.setReceivers(getRecordControlService().getVisitors(messageForm.getId(), com.yuanluesoft.cms.smssend.pojo.SmsSendMessage.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SmsSendMessage messageForm = (SmsSendMessage)form;
		com.yuanluesoft.cms.smssend.pojo.SmsSendMessage message = (com.yuanluesoft.cms.smssend.pojo.SmsSendMessage)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			message.setCreatorId(sessionInfo.getUserId()); //创建者ID
			message.setCreator(sessionInfo.getUserName()); //创建者
			message.setUnitId(sessionInfo.getUnitId()); //创建者所在单位ID
			message.setUnitName(sessionInfo.getUnitName()); //创建者所在单位名称
			message.setCreated(DateTimeUtils.now()); //创建时间
		}
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//保存接收人列表
		if(messageForm.getReceivers()!=null && messageForm.getReceivers().getVisitorIds()!=null) {
			getRecordControlService().updateVisitors(message.getId(), com.yuanluesoft.cms.smssend.pojo.SmsSendMessage.class.getName(), messageForm.getReceivers(), RecordControlService.ACCESS_LEVEL_PREREAD);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#isMemberOfProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if("smsEditor".equals(programmingParticipantId) || "smsAuditor".equals(programmingParticipantId)) {
			com.yuanluesoft.cms.smssend.pojo.SmsSendMessage message = (com.yuanluesoft.cms.smssend.pojo.SmsSendMessage)record;
			SmsService smsService = (SmsService)getService("smsService");
			return smsService.isTransactor((message==null ? sessionInfo.getUnitId() : message.getUnitId()), (message==null ? null : message.getSmsBusinessName()), ("smsEditor".equals(programmingParticipantId) ? SmsService.SMS_SEND_EDITOR : SmsService.SMS_SEND_AUDITOR), sessionInfo);
		}
		return super.isMemberOfProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if("smsEditor".equals(programmingParticipantId) || "smsAuditor".equals(programmingParticipantId)) {
			com.yuanluesoft.cms.smssend.pojo.SmsSendMessage message = (com.yuanluesoft.cms.smssend.pojo.SmsSendMessage)record;
			SmsService smsService = (SmsService)getService("smsService");
			return smsService.listTransactors((message==null ? sessionInfo.getUnitId() : message.getUnitId()), (message==null ? null : message.getSmsBusinessName()), ("smsEditor".equals(programmingParticipantId) ? SmsService.SMS_SEND_EDITOR : SmsService.SMS_SEND_AUDITOR));
		}
		return super.listProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeApproval(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeApproval(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		super.beforeApproval(workflowForm, request, record, openMode, sessionInfo);
		workflowForm.setWorkflowApprovalOptions("同意发送,不同意发送");
	}
}