/*
 * Created on 2007-7-3
 *
 */
package com.yuanluesoft.cms.sitemanage.actions.webdirectory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.yuanluesoft.cms.sitemanage.forms.Site;
import com.yuanluesoft.cms.sitemanage.forms.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;

/**
 * 
 * @author linchuan
 * 
 */
public class WebDirectoryAction extends DirectoryAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		WebDirectory directoryForm = (WebDirectory)form;
		directoryForm.setEditorDeleteable('0'); //允许编辑删除,0/从上级继承,1/允许,2/不允许
		directoryForm.setEditorReissueable('0'); //允许编辑撤销,0/从上级继承,1/允许,2/不允许
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		WebDirectory directoryForm = (WebDirectory)form;
		SiteService siteService = (SiteService)getService("siteService");
		//设置同步的栏目信息
		directoryForm.setSynchToDirectoryIds(ListUtils.join(directoryForm.getSynchToDirectories(), "synchDirectoryId", ",", false)); //同步到的其他栏目ID列表
		directoryForm.setSynchToDirectoryNames(siteService.getDirectoryFullNames(directoryForm.getSynchToDirectoryIds(), "/", ",", "site")); //同步到的其他栏目名称列表
		directoryForm.setSynchFromDirectoryIds(ListUtils.join(directoryForm.getSynchFromDirectories(), "directoryId", ",", false)); //同步到本栏目的其他栏目ID列表
		directoryForm.setSynchFromDirectoryNames(siteService.getDirectoryFullNames(directoryForm.getSynchFromDirectoryIds(), "/", ",", "site")); //同步到本栏目的其他栏目名称列表
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		WebDirectory directoryForm = (WebDirectory)formToValidate;
		//检查设置的同步栏目是否包括当前栏目
		String directoryIds = directoryForm.getSynchToDirectoryIds();
		if(directoryForm.getSynchFromDirectoryIds()!=null && !directoryForm.getSynchFromDirectoryIds().equals("")) {
			directoryIds = (directoryIds==null || directoryIds.equals("") ? "" : directoryIds + ",") + directoryForm.getSynchFromDirectoryIds();
		}
		if(directoryIds==null || directoryIds.equals("")) {
			return;
		}
		if(("," + directoryIds + ",").indexOf("," + directoryForm.getId() + ",")!=-1) {
			directoryForm.setError("不能把本身设置为同步的目标");
			throw new ValidateException();
		}
		//检查设置的同步栏目是否包括当前栏目的上级或者下级
		SiteService siteService = (SiteService)getService("siteService");
		List parentDirectories;
		try {
			parentDirectories = siteService.listParentDirectories(OPEN_MODE_CREATE.equals(openMode) ? directoryForm.getParentDirectoryId() : directoryForm.getId(), null);
		}
		catch (ServiceException e) {
			throw new ValidateException();
		}
		String[] ids = directoryIds.split(",");
		for(int i=0; i<ids.length; i++) {
			long id = Long.parseLong(ids[i]);
			//检查是否上级栏目
			if(ListUtils.findObjectByProperty(parentDirectories, "id", new Long(id))!=null) {
				directoryForm.setError("不能把上级栏目为同步的目标");
				throw new ValidateException();
			}
			//检查是否下级栏目
			try {
				if(ListUtils.findObjectByProperty(siteService.listParentDirectories(id, null), "id", new Long(directoryForm.getId()))!=null) {
					directoryForm.setError("不能把下级栏目为同步的目标");
					throw new ValidateException();
				}
			} 
			catch (ServiceException e) {
				throw new ValidateException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.directorymanage.actions.DirectoryAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		SiteService siteService = (SiteService)getService("siteService");
		WebDirectory directoryForm = (WebDirectory)form;
		com.yuanluesoft.cms.sitemanage.pojo.WebDirectory directory = (com.yuanluesoft.cms.sitemanage.pojo.WebDirectory)record;
		//保存同步设置
		siteService.saveSynchDirectories(directory, OPEN_MODE_CREATE.equals(openMode), directoryForm.getSynchToDirectoryIds(), directoryForm.getSynchFromDirectoryIds());
		return record;
	}
    
	/**
	 * 执行流程配置操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward executeWorkflowConfigAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(!"result".equals(forward.getName())) {
    		return forward;
    	}
    	SessionInfo sessionInfo = getSessionInfo(request, response);
    	WebDirectory directoryForm = (WebDirectory)form;
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=siteService&siteId=" + directoryForm.getId();
    	String returnURL = Environment.getWebApplicationUrl() + "/cms/sitemanage/" + (directoryForm instanceof Site ? "site" : "column") + ".shtml?act=edit&id=" + directoryForm.getId();
    	String testURL = Environment.getWebApplicationUrl() + "/cms/siteresource/admin/article.shtml?act=create&columnId=" + directoryForm.getId();
    	if(directoryForm.getWorkflowId()==0) { //没有配置过
    		workflowConfigureService.createWorkflow("cms/siteresource", "admin/article", null, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow("cms/siteresource", "admin/article", null, "" + directoryForm.getWorkflowId(), notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}