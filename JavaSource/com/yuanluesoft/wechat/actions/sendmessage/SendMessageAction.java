package com.yuanluesoft.wechat.actions.sendmessage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.model.WorkflowEntry;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.wechat.forms.SendMessage;
import com.yuanluesoft.wechat.pojo.WechatMessageReceive;
import com.yuanluesoft.wechat.pojo.WechatMessageSend;
import com.yuanluesoft.wechat.pojo.WechatWorkflowConfig;
import com.yuanluesoft.wechat.service.WechatService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class SendMessageAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runSendMessage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, boolean, com.yuanluesoft.jeaf.workflow.actions.WorkflowAction.WorkflowActionParticipantCallback, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		//获取流程配置
		WechatService wechatService = (WechatService)getService("wechatService");
		WechatWorkflowConfig workflowConfig = wechatService.getWorkflowConfig(sessionInfo.getUnitId(), true);
		if(workflowConfig==null) {
			throw new Exception("Approval workflows are not exists.");
		}
		//按ID查找流程
		com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry workflowEntry = getWorkflowExploitService().getWorkflowEntry(workflowConfig.getWorkflowId(), null, (WorkflowData)record, sessionInfo);
		if(workflowEntry==null) {
			throw new Exception("Workflow entry not exists.");
		}
		return new WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if("wechatMessageManager".equals(programmingParticipantId)) {
			SendMessage sendMessageForm = (SendMessage)workflowForm;
			return getOrgService().listDirectoryVisitors(sendMessageForm.getUnitId()==0 ? sessionInfo.getUnitId() : sendMessageForm.getUnitId(), "wechatMessageManager", true, false, 0);
		}
		return super.listProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			//检查用户对所在单位的消息管理权限
			if(getOrgService().checkPopedom(sessionInfo.getUnitId(), "wechatMessageManager", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		SendMessage sendMessageForm = (SendMessage)formToValidate;
		if(sendMessageForm.getReceiveMessageId()==0 && //不是客服消息
		   sendMessageForm.getRangeMode()==1 && //指定用户
		   (sendMessageForm.getGroupIds()==null || sendMessageForm.getGroupIds().isEmpty()) &&
		   (sendMessageForm.getUserIds()==null || sendMessageForm.getUserIds().isEmpty())) {
			throw new ValidateException("未指定用户");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		SendMessage sendMessageForm = (SendMessage)form;
		WechatMessageSend messageSend = (WechatMessageSend)record;
		if((OPEN_MODE_CREATE.equals(openMode) ? sendMessageForm.getReceiveMessageId() : messageSend.getReceiveMessageId())>0) {
			sendMessageForm.getTabs().addTab(1, "received", "接收消息", "sendMessageSource.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		SendMessage sendMessageForm = (SendMessage)form;
		if(sendMessageForm.getReceiveMessageId()>0) { //客服消息
			WechatService wechatService = (WechatService)getService("wechatService");
			sendMessageForm.setMessageReceive((WechatMessageReceive)wechatService.load(WechatMessageReceive.class, sendMessageForm.getReceiveMessageId()));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		SendMessage sendMessageForm = (SendMessage)form;
		if(sendMessageForm.getNews()!=null && sendMessageForm.getNews().size()>=10) {
			sendMessageForm.setAppendNewsDisabled(true);
		}
		if(sendMessageForm.getReceiveMessageId()>0) { //客服消息
			WechatService wechatService = (WechatService)getService("wechatService");
			sendMessageForm.setMessageReceive((WechatMessageReceive)wechatService.load(WechatMessageReceive.class, sendMessageForm.getReceiveMessageId()));
		}
	}
}