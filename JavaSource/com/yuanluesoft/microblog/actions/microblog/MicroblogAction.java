package com.yuanluesoft.microblog.actions.microblog;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.model.WorkflowEntry;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.microblog.forms.Microblog;
import com.yuanluesoft.microblog.pojo.MicroblogPrivateMessage;
import com.yuanluesoft.microblog.pojo.MicroblogWorkflowConfig;
import com.yuanluesoft.microblog.service.MicroblogService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class MicroblogAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runMicroblog";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, boolean, com.yuanluesoft.jeaf.workflow.actions.WorkflowAction.WorkflowActionParticipantCallback, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		//获取流程配置
		MicroblogService microblogService = (MicroblogService)getService("microblogService");
		MicroblogWorkflowConfig workflowConfig = microblogService.getWorkflowConfig(sessionInfo.getUnitId(), true);
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
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if("microblogMessageManager".equals(programmingParticipantId)) {
			Microblog microblogForm = (Microblog)workflowForm;
			return getOrgService().listDirectoryVisitors(microblogForm.getUnitId()==0 ? sessionInfo.getUnitId() : microblogForm.getUnitId(), "microblogMessageManager", true, false, 0);
		}
		return super.listProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		com.yuanluesoft.microblog.pojo.Microblog microblog = (com.yuanluesoft.microblog.pojo.Microblog)record;
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			//检查用户对所在单位的消息管理权限
			if(getOrgService().checkPopedom(sessionInfo.getUnitId(), "microblogMessageManager", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else if(microblog.getPrivateMessageId()==0 && microblog.getSendTime()!=null) { //不是私信,且已发布过
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || getOrgService().checkPopedom(microblog.getUnitId(), "manager", sessionInfo)) {
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
		Microblog microblogForm = (Microblog)formToValidate;
		if(microblogForm.getPrivateMessageId()==0 && //不是私信
		   "groups".equals(microblogForm.getRange()) && //指定分组
		   (microblogForm.getGroupIds()==null || microblogForm.getGroupIds().isEmpty())) {
			throw new ValidateException("未指定用户分组");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Microblog microblogForm = (Microblog)form;
		MicroblogService microblogService = (MicroblogService)getService("microblogService");
		com.yuanluesoft.microblog.pojo.Microblog microblog = (com.yuanluesoft.microblog.pojo.Microblog)record;
		if((OPEN_MODE_CREATE.equals(openMode) ? microblogForm.getPrivateMessageId() : microblog.getPrivateMessageId())>0) {
			microblogForm.getTabs().addTab(1, "received", "私信", "microblogSource.jsp", false);
		}
		boolean unissueEnabled = false;
		boolean reissueEnabled = false;
		if(!OPEN_MODE_CREATE.equals(openMode) && accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE && microblog.getPrivateMessageId()==0 && microblog.getSendTime()!=null) { //不是私信,且已发布过
			if(microblog.getBlogIds()==null || microblog.getBlogIds().isEmpty()) { //已经撤销发布
				reissueEnabled = true;
				microblogForm.setSubForm("microblogEdit.jsp");
			}
			else {
				unissueEnabled = true;
			}
		}
		if(!unissueEnabled) {
			form.getFormActions().removeFormAction("撤销发布");
		}
		if(!reissueEnabled) {
			form.getFormActions().removeFormAction("重新发布");
		}
		//设置短链接最大长度
		microblogForm.setShortUrlMaxLength(microblogService.getShortUrlMaxLength());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Microblog microblogForm = (Microblog)form;
		MicroblogService microblogService = (MicroblogService)getService("microblogService");
		if(microblogForm.getPrivateMessageId()>0) { //私信
			microblogForm.setPrivateMessage((MicroblogPrivateMessage)microblogService.load(MicroblogPrivateMessage.class, microblogForm.getPrivateMessageId()));
		}
		else if(microblogForm.getAccountIds()==null) {
			microblogForm.setAccountIds(ListUtils.join(microblogService.listAccounts(sessionInfo.getUnitId()), "id", ",", true));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Microblog microblogForm = (Microblog)form;
		if(microblogForm.getPrivateMessageId()>0) { //私信
			MicroblogService microblogService = (MicroblogService)getService("microblogService");
			microblogForm.setPrivateMessage((MicroblogPrivateMessage)microblogService.load(MicroblogPrivateMessage.class, microblogForm.getPrivateMessageId()));
		}
	}
}