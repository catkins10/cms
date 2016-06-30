package com.yuanluesoft.appraise.appraiser.actions.importrecipient.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.yuanluesoft.appraise.appraiser.forms.admin.ImportRecipient;
import com.yuanluesoft.appraise.appraiser.pojo.AppraiserImport;
import com.yuanluesoft.appraise.appraiser.service.AppraiserService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Area;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 *
 * @author linchuan
 *
 */
public class ImportRecipientAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName(com.yuanluesoft.jeaf.workflow.form.WorkflowForm)
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runImportRecipient";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			ImportRecipient importRecipientForm = (ImportRecipient)form;
			if(getOrgService().checkPopedom(importRecipientForm.getUnitId(), "appraiseTransactor", sessionInfo)) { //检查用户的评议责任人
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			form.getTabs().addTab(1, "recipients", "服务对象", "recipients.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ImportRecipient importRecipientForm = (ImportRecipient)form;
		OrgService orgService = getOrgService();
		importRecipientForm.setUnitName(orgService.getDirectoryName(importRecipientForm.getUnitId()));
		//获取用户所在地区
		List areas = orgService.listParentDirectories(importRecipientForm.getUnitId(), "area");
		Area area = (Area)areas.get(0);
		importRecipientForm.setAreaId(area.getId());
		importRecipientForm.setAreaName(area.getDirectoryName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		ImportRecipient importRecipientForm = (ImportRecipient)form;
		AppraiserService appraiserService = (AppraiserService)getService("appraiserService");
		importRecipientForm.setRecipients(appraiserService.listImportedRecipients(importRecipientForm.getId()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		if(OPEN_MODE_CREATE.equals(openMode)) {
			ImportRecipient importRecipientForm = (ImportRecipient)formToValidate;
			AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
			List attachments = null;
			try {
				attachments = attachmentService.list("appraise/appraiser", "data", importRecipientForm.getId(), false, 1, request);
			}
			catch(ServiceException e) {
				
			}
			if(attachments==null || attachments.isEmpty()) {
				importRecipientForm.setError("文件未上传");
				throw new ValidateException();
			}
		}
	}
	
	/**
	 * 导入评议员
	 * @param form
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected List doImport(ActionForm form, SessionInfo sessionInfo) throws Exception {
		ImportRecipient importRecipientForm = (ImportRecipient)form;
		AppraiserService appraiserService = (AppraiserService)getService("appraiserService");
		try {
			return appraiserService.importAppraisers(importRecipientForm.getId(), importRecipientForm.getUnitId(), DateTimeUtils.getMonthEnd(), AppraiserService.APPRAISER_TYPE_RECIPIENT, AppraiserService.APPRAISER_STATUS_TOAPPROVAL, sessionInfo);
		}
		catch(ServiceException se) {
			if(se.getMessage()!=null) {
				importRecipientForm.setError(se.getMessage());
				throw new ValidateException();
			}
			throw se;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			AppraiserImport appraiserImport = (AppraiserImport)record;
			List appraisers = doImport(form, sessionInfo);
			appraiserImport.setApproverCount(appraisers==null ? 0 : appraisers.size());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#retrieveWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		//获取工作流人口列表
		List workflowEntries = getWorkflowExploitService().listWorkflowEntries("appraise/appraiser", participantCallback, sessionInfo);
		if(workflowEntries==null || workflowEntries.isEmpty()) {
			throw new Exception("Approval workflows are not exists.");
		}
		//获取流程
		WorkflowEntry workflowEntry = (WorkflowEntry)workflowEntries.get(0);
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		ImportRecipient importRecipientForm = (ImportRecipient)workflowForm;
		OrgService orgService = getOrgService();
		return orgService.listDirectoryVisitors(importRecipientForm.getAreaId(), programmingParticipantId, true, false, 0);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#isMemberOfProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		ImportRecipient importRecipientForm = (ImportRecipient)workflowForm;
		OrgService orgService = getOrgService();
		return orgService.checkPopedom(importRecipientForm.getAreaId(), programmingParticipantId, sessionInfo);
	}
}