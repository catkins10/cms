package com.yuanluesoft.cms.infopublic.actions.admin.publicinfo;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.infopublic.forms.admin.PublicInfo;
import com.yuanluesoft.cms.infopublic.pojo.PublicDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoDirectory;
import com.yuanluesoft.cms.infopublic.pojo.PublicInfoSubjection;
import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.cms.smssend.service.SmsSendService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowInterface;

/**
 * 
 * @author yuanluesoft
 *
 */
public class PublicInfoAction extends WorkflowAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runInfoApproval";
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		//获取目录的流程设置
		PublicInfo publicInfoForm = (PublicInfo)workflowForm;
		long workflowId = getPublicDirectoryService().getApprovalWorkflowId(publicInfoForm.getDirectoryId());
		if(workflowId==0) {
			throw new Exception("Approval workflows are not exists.");
		}
		//按ID查找流程
		WorkflowEntry workflowEntry = getWorkflowExploitService().getWorkflowEntry("" + workflowId, null, (WorkflowData)record, sessionInfo);
		if(workflowEntry==null) {
			throw new Exception("Workflow entry not exists.");
		}
		return new com.yuanluesoft.jeaf.workflow.model.WorkflowEntry(workflowEntry.getWorkflowId(), ((Element)workflowEntry.getActivityEntries().get(0)).getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		List acl = super.getAcl(applicationName, form, record, openMode, sessionInfo);
		com.yuanluesoft.cms.infopublic.pojo.PublicInfo publicInfo = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
		PublicInfo publicInfoForm = (PublicInfo)form;
		PublicDirectoryService publicDirectoryService = getPublicDirectoryService();
		//获取用户的资源发布权限
		String directoryIds = null;
		if(publicInfoForm.getDirectoryFullName()!=null) {
			directoryIds = publicInfoForm.getOtherDirectoryIds();
			directoryIds = publicInfoForm.getDirectoryId() + (directoryIds==null || directoryIds.equals("") ? "" : "," + directoryIds);
		}
		else if(OPEN_MODE_CREATE.equals(openMode)) {
			directoryIds = publicInfoForm.getDirectoryId() + "";
		}
		else {
			directoryIds = ListUtils.join(publicInfo.getSubjections(), "directoryId", ",", false);
		}
		//获取所有所在目录的最小权限
		boolean isFirstDirectoryManager = false;
		boolean isManager = true;
		boolean isEditor = true;
		String[] ids = directoryIds.split(",");
		for(int i=0; i<ids.length && (isManager || isEditor); i++) {
			List popedoms = publicDirectoryService.listDirectoryPopedoms(Long.parseLong(ids[i]), sessionInfo);
			if(isManager) {
				isManager = (popedoms!=null && popedoms.contains("manager"));
			}
			if(isEditor) {
				isEditor = (popedoms!=null && popedoms.contains("editor"));
			}
			if(i==0) {
				isFirstDirectoryManager = (popedoms!=null && popedoms.contains("manager"));
			}
		}
		if(isEditor) {
			acl.add("editor");
		}
		if(isManager) {
			acl.add("manager");
		}
		if(publicInfo!=null && (publicInfo.getIssuePersonId()>0 || publicInfo.getStatus()==PublicInfoService.INFO_STATUS_NOPASS)) { //已发布过,或者未审批通过
			if(isManager || //允许管理员撤销发布和重新发布
			   (publicInfo!=null && publicInfo.getIssuePersonId()==sessionInfo.getUserId()) || //允许发布人撤销发布和重新发布
			   (publicInfo.getStatus()!=PublicInfoService.INFO_STATUS_NOPASS && isEditor && (isFirstDirectoryManager || publicDirectoryService.isEditorReissueable(new Long(ids[0]).longValue())))) { //编辑,且允许编辑撤销发布和重新发布
				if(publicInfo.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) {
					acl.add("unissue");
				}
				else if(publicInfo.getStatus()==PublicInfoService.INFO_STATUS_UNISSUE || publicInfo.getStatus()==PublicInfoService.INFO_STATUS_NOPASS) {
					acl.add("reissue");
				}
			}
		}
		return acl;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			if(acl.contains("editor")) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else if(acl.contains("unissue") || acl.contains("reissue")) { //撤销发布或重发布
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		else if(record!=null) {
			com.yuanluesoft.cms.infopublic.pojo.PublicInfo publicInfo = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
			if(publicInfo.getStatus()>=PublicInfoService.INFO_STATUS_DELETED) { //已经删除
				if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
				throw new PrivilegeException();
			}
			if(publicInfo.getIssuePersonId()>0) { //已经发布过
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		com.yuanluesoft.cms.infopublic.pojo.PublicInfo info = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
		if(info.getStatus()>=PublicInfoService.INFO_STATUS_DELETED) { //逻辑删除
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
				return;
			}
			throw new PrivilegeException(); //导入的数据,不允许删除
		}
		if(acl.contains("manager")) { //管理员
			return;
		}
		if(acl.contains("editor")) { //编辑
			PublicInfoSubjection subjection = (PublicInfoSubjection)info.getSubjections().iterator().next();
			try {
				if(getPublicDirectoryService().isEditorDeleteable(subjection.getDirectoryId())) {
					return;
				}
			}
			catch (ServiceException e) {
				
			}
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		PublicInfo publicInfoForm = (PublicInfo)workflowForm;
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		return "directoryEditor".equals(programmingParticipantId) ? publicDirectoryService.listDirectoryEditors(publicInfoForm.getDirectoryId(), false, false, 0) : publicDirectoryService.listDirectoryManagers(publicInfoForm.getDirectoryId(), false, false, 0);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		PublicInfo publicInfoForm = (PublicInfo)form;
		com.yuanluesoft.cms.infopublic.pojo.PublicInfo info = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
		String formType = ""; //表单类型,默认/单列(漳州市府办)
		if(info!=null && (acl.contains("reissue") || acl.contains("unissue"))) {
			publicInfoForm.setDirectoryId(((PublicInfoSubjection)info.getSubjections().iterator().next()).getDirectoryId());
			com.yuanluesoft.jeaf.workflow.model.WorkflowEntry workflowEntry = getWorkflowEntry(publicInfoForm, request, record, openMode, true, null, sessionInfo);
			//获取流程界面
			WorkflowInterface workflowInterface = getWorkflowExploitService().previewWorkflowInterface(workflowEntry.getWorkflowDefinitionId(), workflowEntry.getActivityDefinitionId(), false, sessionInfo);
			if(workflowInterface.getSubForm()!=null && workflowInterface.getSubForm().indexOf("SingleColumn")!=-1) {
				formType = "SingleColumn";
			}
		}
		if(acl.contains("reissue")) {
			form.setSubForm("publicInfo" + formType + "Edit.jsp");
		}
		else if(acl.contains("unissue")) {
			form.setSubForm("publicInfo" + formType + "Read.jsp");
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			form.getTabs().removeTab("workflowLog");
		}
		if(record!=null) {
			if(info.getStatus()>=PublicInfoService.INFO_STATUS_DELETED) {
				form.getFormActions().removeFormAction("删除");
				form.getFormActions().removeFormAction("保存");
				form.setSubForm("publicInfoRead.jsp");
			}
			else {
				form.getFormActions().removeFormAction("永久删除");
				form.getFormActions().removeFormAction("撤销删除");
			}
		}
		//添加短信发送操作
		boolean deleteSendSmsAction = true;
		if(record!=null && info.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) { //已经发布
			try {
				SmsSendService smsSendService = (SmsSendService)getService("smsSendService");
				WorkflowEntry workflowEntry  = smsSendService.getSmsSendWorkflowEntry(sessionInfo);
				if(workflowEntry!=null) {
					deleteSendSmsAction = false;
					String smsContent = "信息《" + info.getSubject() + "》已发布，" + 
										(info.getSummarize()==null || info.getSummarize().isEmpty() ? "" : "内容概述：" + info.getSummarize() + "，") +
										"详情请登录网站查询。";
					String execute = "PageUtils.newrecord('cms/smssend', 'admin/message', 'mode=fullscreen'," +
									 "'workflowId=" + workflowEntry.getWorkflowId() +
									 "&activityId=" + ((Element)workflowEntry.getActivityEntries().get(0)).getId() +
									 "&content=" + URLEncoder.encode(smsContent, "utf-8") +
									 "')";
					form.getFormActions().addFormAction(-1, "发送短信", execute, false);
					//FormAction action = (FormAction)ListUtils.findObjectByProperty(form.getFormActions(), "title", "发送短信");
					//action.setExecute(action.getExecute().replaceFirst("workflowId=", "workflowId=" + workflowEntry.getWorkflowId()).replaceFirst("activityId=", "activityId=" + ((Element)workflowEntry.getActivityEntries().get(0)).getId()));
				}
			}
			catch(Exception e) {
				
			}
		}
		if(deleteSendSmsAction) {
			form.getFormActions().removeFormAction("发送短信");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		PublicInfo publicInfoForm = (PublicInfo)form;
		boolean first = true;
		String otherNames = null;
		String otherIds = null;
		for(Iterator iterator = publicInfoForm.getSubjections().iterator(); iterator.hasNext();) {
			PublicInfoSubjection subjection = (PublicInfoSubjection)iterator.next();
			try {
				String directoryName = getPublicDirectoryService().getDirectoryFullName(subjection.getDirectoryId(), "/", "main");
				if(first) {
					first = false;
					//设置所在目录名称
					publicInfoForm.setDirectoryFullName(directoryName);
					publicInfoForm.setDirectoryId(subjection.getDirectoryId());
				}
				else {
					otherIds = (otherIds==null ? "" : otherIds + ",") + subjection.getDirectoryId();
					otherNames = (otherNames==null ? "" : otherNames + ",") + directoryName;
				}
			}
			catch(Exception e) {
				
			}
		}
		//设置所在的其他目录名称
		publicInfoForm.setOtherDirectoryIds(otherIds);
		publicInfoForm.setOtherDirectoryFullNames(otherNames);
		setIssueSiteNames(publicInfoForm); //设置同步的网站栏目名称
		//获取目录所属站点
		PublicMainDirectory mainDirectory = getPublicDirectoryService().getMainDirectory(publicInfoForm.getDirectoryId());
		publicInfoForm.setSiteId(mainDirectory==null ? 0 : mainDirectory.getSiteId());
		//如果是管理员,设置访问者列表
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
		if(publicDirectoryService.checkPopedom(publicInfoForm.getDirectoryId(), "manager", sessionInfo)) {
			publicInfoForm.setReaders(getRecordControlService().getVisitors(publicInfoForm.getId(), com.yuanluesoft.cms.infopublic.pojo.PublicInfo.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		PublicDirectoryService directoryService = getPublicDirectoryService();
		PublicInfo publicInfoForm = (PublicInfo)form;
		publicInfoForm.setCreated(DateTimeUtils.now());
		//设置所在栏目
		publicInfoForm.setDirectoryFullName(getPublicDirectoryService().getDirectoryFullName(publicInfoForm.getDirectoryId(), "/", "main"));
		//获取目录
		PublicDirectory publicDirectory = (PublicDirectory)directoryService.getDirectory(publicInfoForm.getDirectoryId());
		publicInfoForm.setType(publicDirectory instanceof PublicInfoDirectory ? PublicInfoService.INFO_TYPE_NORMAL : PublicInfoService.INFO_TYPE_ARTICLE); //设置信息类型
		//设置主题分类
		//publicInfoForm.setCategory(publicInfoForm.getDirectoryFullName().substring(publicInfoForm.getDirectoryFullName().lastIndexOf('/') + 1));
		//设置发布机构
		if(publicInfoForm.getInfoFrom()==null) {
			Org org = getOrgService().getOrg(sessionInfo.getUnitId());
			String from = null;
			if(org!=null) {
				if(org instanceof Unit) {
					from = ((Unit)org).getFullName();
				}
				if(from==null || "".equals(from)) {
					from = org.getDirectoryName();
				}
			}
			publicInfoForm.setInfoFrom(from);
			publicInfoForm.setInfoFromUnitId(sessionInfo.getUnitId());
		}
		publicInfoForm.setIssueSite('1');
		//获取目录信息
		if(publicInfoForm.getIssueSiteIds()==null) {
			publicInfoForm.setIssueSiteIds(directoryService.getDirectorySynchSiteIds(publicInfoForm.getDirectoryId(), true));
		}
		setIssueSiteNames(publicInfoForm); //设置同步的网站栏目名称
		PublicMainDirectory mainDirectory = getPublicDirectoryService().getMainDirectory(publicInfoForm.getDirectoryId());
		publicInfoForm.setSiteId(mainDirectory==null ? 0 : mainDirectory.getSiteId());
	}
	
	/**
	 * 设置同步的网站栏目名称
	 * @param publicInfoForm
	 * @throws Exception
	 */
	private void setIssueSiteNames(PublicInfo publicInfoForm) throws Exception {
		SiteService siteService = (SiteService)getService("siteService");
		if(publicInfoForm.getStatus()==PublicInfoService.INFO_STATUS_ISSUE) { //已发布
			SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
			publicInfoForm.setIssueSiteIds(ListUtils.join(siteResourceService.listColumnIdsBySourceRecordId(publicInfoForm.getId() + ""), ",", false));
			publicInfoForm.setIssueSiteNames(siteService.getDirectoryFullNames(publicInfoForm.getIssueSiteIds(), "/", ",", "site"));
			return;
		}
		if(publicInfoForm.getIssueSiteIds()==null || publicInfoForm.getIssueSiteIds().isEmpty()) {
			return;
		}
		String issueSiteIds = null;
		String issueSiteNames = null;
		String[] ids = publicInfoForm.getIssueSiteIds().split(",");
		for(int i=0; i<ids.length; i++) {
			try {
				issueSiteNames = (issueSiteNames==null ? "" : issueSiteNames + ",") + siteService.getDirectoryFullName(Long.parseLong(ids[i]), "/", "site");
				issueSiteIds = (issueSiteIds==null ? "" : issueSiteIds + ",") + ids[i];
			}
			catch(Exception e) {
				
			}
		}
		publicInfoForm.setIssueSiteIds(issueSiteIds);
		publicInfoForm.setIssueSiteNames(issueSiteNames);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		com.yuanluesoft.cms.infopublic.pojo.PublicInfo info = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
		if(info.getStatus()!=PublicInfoService.INFO_STATUS_ISSUE) { //未发布
			if(workflowInstanceCompleted) { //流程结束
				info.setStatus(PublicInfoService.INFO_STATUS_NOPASS); //办结/未通过审核
			}
			else if(isReverse) { //回退
				info.setStatus(PublicInfoService.INFO_STATUS_UNDO); //回退/取回
			}
			else { //流转中
				info.setStatus(PublicInfoService.INFO_STATUS_TODO); //回退/取回
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#runDoReverse(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void runDoReverse(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.runDoReverse(workflowForm, record, request, response, sessionInfo);
		com.yuanluesoft.cms.infopublic.pojo.PublicInfo info = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
		info.setStatus(PublicInfoService.INFO_STATUS_UNDO); //回退/取回
		saveRecord(workflowForm, record, OPEN_MODE_EDIT, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#runDoUndo(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void runDoUndo(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.runDoUndo(workflowForm, record, request, response, sessionInfo);
		com.yuanluesoft.cms.infopublic.pojo.PublicInfo info = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
		info.setStatus(PublicInfoService.INFO_STATUS_UNDO); //回退/取回
		saveRecord(workflowForm, record, OPEN_MODE_EDIT, request, response, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		PublicInfo publicInfoForm = (PublicInfo)formToValidate;
		if(publicInfoForm.getIssueTime()!=null && publicInfoForm.getIssueTime().after(DateTimeUtils.now())) {
			publicInfoForm.setError("发布时间不能在当前时间之后");
			throw new ValidateException();
		}
		if(publicInfoForm.getGenerateDate()!=null && publicInfoForm.getGenerateDate().after(DateTimeUtils.now())) {
			publicInfoForm.setError("生成日期不能在当前时间之后");
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.cms.infopublic.pojo.PublicInfo info = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
		PublicInfo publicInfoForm = (PublicInfo)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(info.getCreated()==null) {
				info.setCreated(DateTimeUtils.now());
			}
			info.setCreatorId(sessionInfo.getUserId()); //创建人ID
			info.setCreator(sessionInfo.getUserName()); //创建人
			info.setOrgName(sessionInfo.getDepartmentName()); //创建人所在部门名称
			info.setOrgId(sessionInfo.getDepartmentId()); //创建人所在部门ID
			info.setUnitName(getOrgService().getOrg(sessionInfo.getUnitId()).getDirectoryName()); //创建人所在单位名称
			info.setUnitId(sessionInfo.getUnitId()); //创建人所在单位ID
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(publicInfoForm.getDirectoryFullName()!=null) {
			PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
			//保存所属栏目列表
			String directoryIds = publicInfoForm.getOtherDirectoryIds();
			directoryIds = publicInfoForm.getDirectoryId() + (directoryIds==null || directoryIds.equals("") ? "" : "," + directoryIds);
			publicInfoService.updateInfoSubjections(info, OPEN_MODE_CREATE.equals(openMode), directoryIds);
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
		com.yuanluesoft.cms.infopublic.pojo.PublicInfo info = (com.yuanluesoft.cms.infopublic.pojo.PublicInfo)record;
		if(!publicInfoService.isLogicalDelete() || "true".equals(request.getParameter("physical"))) { //物理删除
			super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
		}
		else if(info.getStatus()<PublicInfoService.INFO_STATUS_DELETED) { //逻辑删除
			//挂起流程实例
			getWorkflowExploitService().suspendWorkflowInstance(info.getWorkflowInstanceId(), info, sessionInfo);
			info.setStatus((char)(PublicInfoService.INFO_STATUS_DELETED + (info.getStatus() - '0')));
			publicInfoService.update(info);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getWaterMark(com.yuanluesoft.jeaf.attachmentmanage.model.AttachmentConfig, com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	public WaterMark getWaterMark(ActionForm form, HttpServletRequest request) throws Exception {
		PublicInfo publicInfoForm = (PublicInfo)form;
		PublicMainDirectory mainDirectory = getPublicDirectoryService().getMainDirectory(publicInfoForm.getDirectoryId());
		return ((SiteService)getService("siteService")).getWaterMark(mainDirectory==null ? 0 : mainDirectory.getSiteId());
	}

	/**
	 * 获取目录服务
	 * @return
	 * @throws SystemUnregistException
	 */
	private PublicDirectoryService getPublicDirectoryService() throws SystemUnregistException {
		return (PublicDirectoryService)getService("publicDirectoryService");
	}
	
	/**
	 * 添加“添加下一篇”按钮
	 * @param publicInfoForm
	 * @throws Exception
	 */
	protected void addCreateNextAction(PublicInfo publicInfoForm) throws Exception {
		if(publicInfoForm.getBatchIds()==null || publicInfoForm.getBatchIds().isEmpty()) { //不是批量处理
			String url = "window.top.location='publicInfo.shtml" +
						 "?act=create" +
						 "&directoryId=" + publicInfoForm.getDirectoryId() +
						 (publicInfoForm.getMark()==null ? "" : "&mark=" + URLEncoder.encode(publicInfoForm.getMark(), "utf-8")) + //文号
						 (publicInfoForm.getKeywords()==null ? "" : "&keywords=" + URLEncoder.encode(publicInfoForm.getKeywords(), "utf-8")) + //主题词
						 "&infoFromUnitId=" + publicInfoForm.getInfoFromUnitId() + //发布机构ID
						 (publicInfoForm.getInfoFrom()==null ? "" : "&infoFrom=" + URLEncoder.encode(publicInfoForm.getInfoFrom(), "utf-8")) + //发布机构
						 (publicInfoForm.getCategory()==null ? "" : "&category=" + URLEncoder.encode(publicInfoForm.getCategory(), "utf-8")) + //分类
						 (publicInfoForm.getGenerateDate()==null ? "" : "&generateDate=" + URLEncoder.encode(DateTimeUtils.formatDate(publicInfoForm.getGenerateDate(), null), "utf-8")) + //发文时间
						 "&seq=" + UUIDLongGenerator.generateId() + "';";
			publicInfoForm.getFormActions().addFormAction(0, "添加下一篇", url, false);
		}
	}
}