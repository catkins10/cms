package com.yuanluesoft.cms.publicservice.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;
import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public abstract class PublicServiceAdminAction extends WorkflowAction {
	protected boolean sendSmsAfterCompleted = true; //是否在办结后给提交人发送短信,默认是发送
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		List acl =  super.getAcl(applicationName, form, record, openMode, sessionInfo);
		//检查是否站点管理员
		PublicService publicService = (PublicService)record;
		PublicServiceAdminForm publicServiceForm = (PublicServiceAdminForm)form;
		SiteService siteService = (SiteService)getService("siteService");
		if(siteService.checkPopedom(publicService==null ? publicServiceForm.getSiteId() : publicService.getSiteId(), "manager", sessionInfo)) {
			acl.add("site_manager");
		}
		//检查用户是否公开发布人
		if(publicService!=null && publicService.getPublicPersonId()==sessionInfo.getUserId()) {
			acl.add("publish");
		}
		return acl;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager") || acl.contains("site_manager")) { //允许管理员编辑
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		PublicService publicService = (PublicService)record;
		if(publicService.getIsDeleted()==1) { //逻辑删除
			throw new PrivilegeException();
		}
		if(acl.contains("publish")) { //有发布权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager") || acl.contains("site_manager")) { //允许管理员删除
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		PublicServiceAdminForm publicServiceAdminForm = (PublicServiceAdminForm)workflowForm;
		SiteService siteService = (SiteService)getService("siteService");
		if("siteEditor".equals(programmingParticipantId)) {
			return siteService.listSiteEditors(publicServiceAdminForm.getSiteId(), false, false, 0);
		}
		else if("siteManager".equals(programmingParticipantId)) {
			siteService.listSiteManagers(publicServiceAdminForm.getSiteId(), false, false, 0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm,  boolean, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		if(sendSmsAfterCompleted && workflowInstanceCompleted) { //流程已经结束
			//给提交人发送短信
			com.yuanluesoft.cms.publicservice.service.PublicService publicService = (com.yuanluesoft.cms.publicservice.service.PublicService)getService("publicService");
			publicService.sendSmsToCreator(workflowForm.getFormDefine().getApplicationName(), workflowForm.getId());
			PublicService publicServiceRecord = (PublicService)record;
			publicServiceRecord.setCompleteTime(DateTimeUtils.now()); //办结时间
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		SiteService siteService = (SiteService)getService("siteService");
		PublicServiceAdminForm publicServiceAdminForm = (PublicServiceAdminForm)form;
		//设置站点名称
		WebDirectory webDirectory = (WebDirectory)siteService.getDirectory(publicServiceAdminForm.getSiteId());
		publicServiceAdminForm.setSiteName(webDirectory==null ? null : webDirectory.getDirectoryName());
		//设置访问者列表
		if(siteService.checkPopedom(publicServiceAdminForm.getSiteId(), "manager", sessionInfo)) {
			publicServiceAdminForm.setReaders(getRecordControlService().getVisitors(publicServiceAdminForm.getId(), PublicService.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(record!=null) {
			PublicService publicService = (PublicService)record;
			if(publicService.getIsDeleted()==1) { //逻辑删除
				form.getFormActions().removeFormAction("删除");
				form.getFormActions().removeFormAction("保存");
				form.setSubForm(form.getSubForm().replace("Edit", "Read"));
			}
			else {
				form.getFormActions().removeFormAction("永久删除");
				form.getFormActions().removeFormAction("撤销删除");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if("true".equals(request.getParameter("physical"))) { //物理删除
			super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
		}
		else { //逻辑删除
			PublicService publicService = (PublicService)record;
			getWorkflowExploitService().suspendWorkflowInstance(publicService.getWorkflowInstanceId(), publicService, sessionInfo); //挂起流程
			publicService.setIsDeleted(1);
			getBusinessService(formDefine).update(publicService);
		}
	}
	
	/**
	 * 执行撤销删除操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeUndeleteAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("undelete", Boolean.TRUE);
		return executeSaveAction(mapping, form, request, response, true, null, null, null);
	}

	/**
	 * 执行设置发布选项操作
	 * @param isAlwaysPublishAll
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeSetPublishOptionAction(boolean isAlwaysPublishAll, ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = executeSaveAction(mapping, form, request, response, true, null, null, null);
		PublicServiceAdminForm publicServiceAdminForm = (PublicServiceAdminForm)form;
		publicServiceAdminForm.setAlwaysPublishAll(isAlwaysPublishAll);
		if(publicServiceAdminForm.getPublicSubject()==null) {
			publicServiceAdminForm.setPublicSubject(publicServiceAdminForm.getSubject());
		}
		if(publicServiceAdminForm.getPublicPass()!='1') {
			publicServiceAdminForm.setPublicPass('0');
		}
		if(publicServiceAdminForm.getPublicBody()!='1') {
			publicServiceAdminForm.setPublicBody('0');
		}
		if(publicServiceAdminForm.getPublicWorkflow()!='1') {
			publicServiceAdminForm.setPublicWorkflow('0');
		}
		//设置发布选择内置对话框
		publicServiceAdminForm.setFormTitle("发布选项");
		publicServiceAdminForm.setInnerDialog("/cms/publicservice/admin/publishOption.jsp");
		publicServiceAdminForm.getFormActions().addFormAction(-1, "完成", "FormUtils.doAction('publish')", true);
		addReloadAction(publicServiceAdminForm, "取消", request, -1, false); //取消
		return forward;
	}

	/**
	 * 执行发布操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executePublishAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("publish", Boolean.TRUE);
		return executeSaveAction(mapping, form, request, response, false, null, "发布完成！", null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		PublicService publicService = (PublicService)record;
		if(Boolean.TRUE.equals(request.getAttribute("publish"))) {
			PublicServiceAdminForm publicServiceAdminForm = (PublicServiceAdminForm)form;
			//记录发布人信息
			if(publicService.getPublicPersonId()==0) {
				publicService.setPublicPersonId(sessionInfo.getUserId()); //发布人ID
				publicService.setPublicPersonName(sessionInfo.getUserName()); //发布人
			}
			if(publicServiceAdminForm.isAlwaysPublishAll()) { //总是公布全部信息
				publicService.setPublicBody(publicService.getPublicPass());
				publicService.setPublicWorkflow(publicService.getPublicPass());
			}
		}
		else if(Boolean.TRUE.equals(request.getAttribute("undelete"))) { //撤销删除
			getWorkflowExploitService().resumeWorkflowInstance(publicService.getWorkflowInstanceId(), publicService, sessionInfo); //恢复流程
			publicService.setIsDeleted(0);
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}