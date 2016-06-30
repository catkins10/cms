/*
 * Created on 2007-7-17
 *
 */
package com.yuanluesoft.cms.siteresource.actions.admin;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.forms.Resource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
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
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author linchuan
 *
 */
public abstract class ResourceAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#retrieveWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected com.yuanluesoft.jeaf.workflow.model.WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		//获取站点的流程配置
		Resource resourceForm = (Resource)workflowForm;
		long workflowId = getSiteService().getApprovalWorkflowId(resourceForm.getColumnId());
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
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Record record = super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(record==null || !(record instanceof SiteResource)) {
			return record;
		}
		SiteService siteService = getSiteService();
		SiteResource resource = (SiteResource)record;
		SiteResourceSubjection subjection = (SiteResourceSubjection)resource.getSubjections().iterator().next();
		String[] otherColumnIds = resource.getOtherColumnIds()==null || resource.getOtherColumnIds().isEmpty() ? null : resource.getOtherColumnIds().split(",");
		resource.setOtherColumnIds(null);
		resource.setOtherColumnNames(null);
		for(int i=0; i<(otherColumnIds==null ? 0 : otherColumnIds.length); i++) {
			long columnId = Long.parseLong(otherColumnIds[i]);
			if(columnId==subjection.getSiteId()) {
				continue;
			}
			String columnName = siteService.getDirectoryFullName(columnId, "/", "site");
			if(columnName!=null && !columnName.isEmpty()) {
				resource.setOtherColumnIds((resource.getOtherColumnIds()==null ? "" : resource.getOtherColumnIds() + ",") + otherColumnIds[i]);
				resource.setOtherColumnNames((resource.getOtherColumnNames()==null ? "" : resource.getOtherColumnNames() + ",") + columnName);
			}
		}
		return resource;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		List acl = super.getAcl("cms/sitemanage", form, record, openMode, sessionInfo);
		SiteResource resource = (SiteResource)record;
		Resource resourceForm = (Resource)form;
		SiteService siteService = getSiteService();
		//获取用户的资源发布权限
		String columnIds = null;
		if(resourceForm.getColumnFullName()!=null) {
			columnIds = resourceForm.getOtherColumnIds();
			columnIds = resourceForm.getColumnId() + (columnIds==null || columnIds.equals("") ? "" : "," + columnIds);
		}
		else if(OPEN_MODE_CREATE.equals(openMode)) {
			columnIds = resourceForm.getColumnId() + "";
		}
		else if(resource.getOtherColumnIds()==null || resource.getOtherColumnIds().isEmpty()) {
			columnIds = ListUtils.join(resource.getSubjections(), "siteId", ",", false);
		}
		else {
			columnIds = ((SiteResourceSubjection)resource.getSubjections().iterator().next()).getSiteId() + "," + resource.getOtherColumnIds();
		}
		//获取栏目需要同步的其他栏目ID列表
		List synchColumnIds = siteService.listSynchColumnIds(columnIds.split(",")[0]);
		//获取所有所在站点的最小权限
		boolean isManager = true;
		boolean isEditor = true;
		boolean isFirstColumnManager = false;
		String[] ids = columnIds.split(",");
		for(int i=0; i<ids.length && (isManager || isEditor); i++) {
			Long directoryId = new Long(ids[i]);
			if(i>0 && synchColumnIds.indexOf(directoryId)!=-1) { //隶属于栏目需要同步的栏目
				continue; //不检查权限
			}
			List popedoms = siteService.listDirectoryPopedoms(directoryId.longValue(), sessionInfo);
			if(popedoms==null || popedoms.isEmpty()) {
				isManager = false;
				isEditor = false;
				continue;
			}
			if(isManager) {
				isManager = popedoms.contains("manager");
			}
			if(isEditor) {
				isEditor = popedoms.contains("editor");
			}
			if(i==0) {
				isFirstColumnManager = popedoms.contains("manager");
			}
		}
		if(isEditor) {
			acl.add("editor");
		}
		if(isManager) {
			acl.add("manager");
		}
		boolean unissue = false; //是否允许撤销发布和重新发布
		if((resource!=null && isManager) || //管理员
		   (resource!=null && isEditor && //编辑
			resource.getStatus()!=SiteResourceService.RESOURCE_STATUS_NOPASS && //不是办结未发布
		    (isFirstColumnManager || siteService.isEditorReissueable(new Long(ids[0]).longValue())))) { //栏目管理员或者允许编辑撤销发布和重新发布
			unissue = true;
			acl.add("setTop"); //添加置顶权限
		}
		if(unissue || (resource!=null && resource.getIssuePersonId()==sessionInfo.getUserId())) { //允许发布人撤销发布和重新发布
			if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_ISSUE) {
				acl.add("unissue");
			}
			else if(resource.getStatus()==SiteResourceService.RESOURCE_STATUS_UNISSUE || resource.getStatus()==SiteResourceService.RESOURCE_STATUS_NOPASS) {
				acl.add("reissue");
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
			throw new PrivilegeException();
		}
		if(acl.contains("unissue") || acl.contains("reissue")) { //撤销发布或重发布
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		else if(record!=null) {
			SiteResource resource = (SiteResource)record;
			if(resource.getStatus()>=SiteResourceService.RESOURCE_STATUS_DELETED) { //逻辑删除
				if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
				throw new PrivilegeException();
			}
			if(resource.getIssuePersonId()>0) { //已经发布过
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		SiteResource resource = (SiteResource)record;
		if(resource.getStatus()>=SiteResourceService.RESOURCE_STATUS_DELETED) { //逻辑删除
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
				return;
			}
			throw new PrivilegeException(); //导入的数据,不允许删除
		}
		if(acl.contains("manager")) { //管理员
			return;
		}
		if(acl.contains("editor")) { //编辑
			SiteResourceSubjection subjection = (SiteResourceSubjection)resource.getSubjections().iterator().next();
			try {
				if(getSiteService().isEditorDeleteable(subjection.getSiteId())) {
					return;
				}
			} 
			catch (ServiceException e) {
				
			}
		}
		if(resource.getWorkflowInstanceId()==null || resource.getWorkflowInstanceId().isEmpty()) {
			throw new PrivilegeException(); //导入的数据,不允许删除
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Resource resourceForm = (Resource)workflowForm;
		SiteService siteService = (SiteService)getService("siteService");
		return "siteEditor".equals(programmingParticipantId) ? siteService.listSiteEditors(resourceForm.getColumnId(), false, false, 0) : siteService.listSiteManagers(resourceForm.getColumnId(), false, false, 0);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(acl.contains("reissue")) {
			form.setSubForm(form.getSubForm().replace("Read", "Edit"));
		}
		else if(acl.contains("unissue")) {
			form.setSubForm(form.getSubForm().replace("Edit", "Read"));
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			form.getTabs().removeTab("workflowLog");
		}
		if(record!=null) {
			SiteResource resource = (SiteResource)record;
			if(resource.getStatus()>=SiteResourceService.RESOURCE_STATUS_DELETED) { //逻辑删除
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
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Resource resourceForm = (Resource)form;
		resourceForm.setCreated(DateTimeUtils.now());
		SiteService siteService = getSiteService();
		//设置所在栏目名称
		resourceForm.setColumnFullName(getSiteService().getDirectoryFullName(resourceForm.getColumnId(), "/", "site"));
		//设置同步的栏目列表
		List synchColumnIds = siteService.listSynchColumnIds(resourceForm.getColumnId() + "");
		if(resourceForm.getOtherColumnIds()==null) { //设置其他栏目的名称
			resourceForm.setOtherColumnIds(ListUtils.join(synchColumnIds.subList(1, synchColumnIds.size()), ",", false));
			resourceForm.setOtherColumnNames(siteService.getDirectoryFullNames(resourceForm.getOtherColumnIds(), "/", ",", "site"));
		}
		//匿名用户访问级别
		resourceForm.setAnonymousLevel(siteService.getAnonymousLevel(resourceForm.getColumnId()));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		Resource resourceForm = (Resource)formToValidate;
		if(resourceForm.getOtherColumnIds()!=null &&
		   ("," + resourceForm.getOtherColumnIds() + ",").indexOf("," + resourceForm.getColumnId() + ",")!=-1) {
			resourceForm.setError("所属的其他栏目不能包含所属栏目");
			throw new ValidateException();
		}
		if(resourceForm.getIssueTime()!=null && resourceForm.getIssueTime().after(DateTimeUtils.now())) {
			resourceForm.setError("发布时间不能在当前时间之后");
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Resource resourceForm = (Resource)form;
		SiteResource resource = (SiteResource)record;
		String siteIds = null; //隶属站点ID列表
		SiteService siteService = getSiteService();
		boolean isSiteManager = false;
		for(Iterator iterator = resource.getSubjections()==null ? null : resource.getSubjections().iterator(); iterator!=null && iterator.hasNext();) {
			SiteResourceSubjection subjection = (SiteResourceSubjection)iterator.next();
			try {
				long siteId = siteService.getParentSite(subjection.getSiteId()).getId();
				if(siteIds==null) {
					siteIds = "" + siteId;
				}
				else if(("," + siteIds + ",").indexOf("," + siteId + ",")==-1) {
					siteIds += "," + siteId;
				}
				if(!isSiteManager && siteService.checkPopedom(siteId, "manager", sessionInfo)) { //检查用户的站点管理权限
					isSiteManager = true;
				}
				if(resourceForm.getColumnFullName()==null) { //设置所在栏目名称
					resourceForm.setColumnFullName(siteService.getDirectoryFullName(subjection.getSiteId(), "/", "site"));
					resourceForm.setColumnId(subjection.getSiteId());
				}
			}
			catch(Exception e) {
				continue;
			}
		}
		//设置父站点ID
		resourceForm.setSiteId(Long.parseLong(siteIds.split(",")[0]));
		resourceForm.setSiteIds(siteIds);
	
		if(isSiteManager) { //站点管理员
			//输出访问者列表
			resourceForm.setReaders(getRecordControlService().getVisitors(resourceForm.getId(), SiteResource.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY));
		}
		if(!isSiteManager || resource.getStatus()!=SiteResourceService.RESOURCE_STATUS_ISSUE) { //不是站点管理员,或者未发布
			//删除头版头条按钮
			resourceForm.getFormActions().removeFormAction("设为头版头条");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		SiteResource resource = (SiteResource)record;
		if(resource.getStatus()!=SiteResourceService.RESOURCE_STATUS_ISSUE) { //未发布
			if(workflowInstanceCompleted) { //流程结束
				resource.setStatus(SiteResourceService.RESOURCE_STATUS_NOPASS); //办结/未通过审核
			}
			else if(isReverse) { //回退
				resource.setStatus(SiteResourceService.RESOURCE_STATUS_UNDO); //回退/取回
			}
			else { //流转中
				resource.setStatus(SiteResourceService.RESOURCE_STATUS_TODO); //回退/取回
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#runDoReverse(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void runDoReverse(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.runDoReverse(workflowForm, record, request, response, sessionInfo);
		SiteResource resource = (SiteResource)record;
		resource.setStatus(SiteResourceService.RESOURCE_STATUS_UNDO); //回退/取回
		saveRecord(workflowForm, record, OPEN_MODE_EDIT, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#runDoUndo(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void runDoUndo(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.runDoUndo(workflowForm, record, request, response, sessionInfo);
		SiteResource resource = (SiteResource)record;
		resource.setStatus(SiteResourceService.RESOURCE_STATUS_UNDO); //回退/取回
		saveRecord(workflowForm, record, OPEN_MODE_EDIT, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SiteResource resource = (SiteResource)record;
		Resource resourceForm = (Resource)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			resource.setCreated(DateTimeUtils.now());
			resource.setEditorId(sessionInfo.getUserId()); //编辑ID
			resource.setEditor(sessionInfo.getUserName()); //编辑
			resource.setOrgName(sessionInfo.getDepartmentName()); //创建人所在部门名称
			resource.setOrgId(sessionInfo.getDepartmentId()); //创建人所在部门ID
			resource.setUnitName(getOrgService().getOrg(sessionInfo.getUnitId()).getDirectoryName()); //创建人所在单位名称
			resource.setUnitId(sessionInfo.getUnitId()); //创建人所在单位ID
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(resourceForm.getColumnFullName()!=null && resource.getStatus()!=SiteResourceService.RESOURCE_STATUS_ISSUE) {
			SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
			siteResourceService.updateSiteResourceSubjections(resource, OPEN_MODE_CREATE.equals(openMode), "" + resourceForm.getColumnId());
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		SiteResource resource = (SiteResource)record;
		if(!siteResourceService.isLogicalDelete() || "true".equals(request.getParameter("physical"))) { //物理删除
			super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
		}
		else if(resource.getStatus()<SiteResourceService.RESOURCE_STATUS_DELETED) { //逻辑删除
			//挂起流程实例
			getWorkflowExploitService().suspendWorkflowInstance(resource.getWorkflowInstanceId(), resource, sessionInfo);
			resource.setStatus((char)(SiteResourceService.RESOURCE_STATUS_DELETED + (resource.getStatus() - '0')));
			siteResourceService.update(resource);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getWaterMark(com.yuanluesoft.jeaf.attachmentmanage.model.AttachmentConfig, com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	public WaterMark getWaterMark(ActionForm form, HttpServletRequest request) throws Exception {
		Resource resourceForm = (Resource)form;
		return getSiteService().getWaterMark(resourceForm.getColumnId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#executeDeleteAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String, java.lang.String)
	 */
	public ActionForward executeDeleteAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult, String forwardName) throws Exception {
		Resource resourceForm = (Resource)form;
		if(resourceForm.getDeleteFromColumn()!=0) { //从栏目中删除
			String siteIds = "," + resourceForm.getColumnId() +  "," + resourceForm.getOtherColumnIds() + ",";
			siteIds = siteIds.replaceFirst("," + resourceForm.getDeleteFromColumn() + ",", ",");
			siteIds = siteIds.substring(1, siteIds.length()-1);
			int index = siteIds.indexOf(',');
			if(index==-1) {
				resourceForm.setColumnId(Long.parseLong(siteIds));
				resourceForm.setOtherColumnIds(null);
			}
			else {
				resourceForm.setColumnId(Long.parseLong(siteIds.substring(0, index)));
				resourceForm.setOtherColumnIds(siteIds.substring(index + 1));
			}
			return executeSaveAction(mapping, form, request, response, false, null, "删除成功", forwardName);
		}
		return super.executeDeleteAction(mapping, form, request, response, actionResult, forwardName);
	}

	/**
	 * 获取站点服务
	 * @return
	 * @throws SystemUnregistException
	 */
	private SiteService getSiteService() throws SystemUnregistException {
		return (SiteService)getService("siteService");
	}
}