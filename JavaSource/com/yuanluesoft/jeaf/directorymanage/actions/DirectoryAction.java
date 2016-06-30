package com.yuanluesoft.jeaf.directorymanage.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.forms.DirectoryForm;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomConfig;
import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryLink;
import com.yuanluesoft.jeaf.directorymanage.service.DirectoryService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public abstract class DirectoryAction extends FormAction {

	/**
	 * 获取有编辑权限的权限名称列表
	 * @return
	 */
	protected String getEditablePopedomNames() {
		return "manager";
	}
	
	/**
	 * 获取有查看权限的权限名称列表
	 * @return
	 */
	protected String getReadablePopedomNames() {
		return "all";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		DirectoryForm directoryForm = (DirectoryForm)form;
		DirectoryService directoryService = (DirectoryService)getBusinessService(((ActionForm)form).getFormDefine());
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			//检查用户是否上级目录的管理员
			if(directoryService.checkPopedom(directoryForm.getParentDirectoryId(), getEditablePopedomNames(), sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else {
			Directory directory = (Directory)record;
			//检查用户对当前目录的管理权限
			if(directoryService.checkPopedom(directory.getId(), getEditablePopedomNames(), sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			//检查用户对当前目录的查看权限
			if(directoryService.checkPopedom(directory.getId(), getReadablePopedomNames(), sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Directory directory = (Directory)record;
		if(directory.getId()==0) {
			throw new PrivilegeException();
		}
		DirectoryService directoryService = (DirectoryService)getBusinessService(((ActionForm)form).getFormDefine());
		//检查用户对上级目录的权限
		if(!directoryService.checkPopedom(directory.getParentDirectoryId(), getEditablePopedomNames(), sessionInfo)) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		//设置权限配置列表
		DirectoryForm directoryForm = (DirectoryForm)form;
		DirectoryService directoryService = (DirectoryService)getBusinessService(form.getFormDefine());
		directoryForm.setPopedomConfigs(directoryService.getDirectoryPopedomConfigs(null, form.getFormDefine().getRecordClassName()));
		//设置父目录名称
		directoryForm.setParentDirectoryName(directoryService.getDirectoryFullName(directoryForm.getParentDirectoryId(),"/", null));
		directoryForm.setCreator(sessionInfo.getUserName()); //创建人
		directoryForm.setCreated(DateTimeUtils.now()); //创建时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		//设置权限配置列表
		DirectoryForm directoryForm = (DirectoryForm)form;
		Directory directory = (Directory)record;
		DirectoryService directoryService = (DirectoryService)getBusinessService(form.getFormDefine());
		directoryForm.setPopedomConfigs(directoryService.getDirectoryPopedomConfigs(directory, form.getFormDefine().getRecordClassName()));
		//设置父目录名称
		directoryForm.setParentDirectoryName(directoryService.getDirectoryFullName(directoryForm.getParentDirectoryId(),"/", null));
		if(accessLevel>RecordControlService.ACCESS_LEVEL_READONLY) { //有编辑权限
			//检查用户是否父目录的管理员,如果不是,禁止修改父目录
			directoryForm.setChangeParentDirectoryDisabled(!directoryService.checkPopedom(directory.getParentDirectoryId(), getEditablePopedomNames(), sessionInfo));
		}
		//设置引用的目录列表
		List childDirectories = directoryService.listChildDirectories(directory.getId(), null, null, null, true, false, null, 0, 0);
		childDirectories = ListUtils.getSubListByType(childDirectories, DirectoryLink.class, true);
		directoryForm.setLinkedDirectoryIds(ListUtils.join(childDirectories, "linkedDirectoryId", ",", false));
		directoryForm.setLinkedDirectoryNames(ListUtils.join(childDirectories, "directoryName", ",", false));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		super.inheritProperties(newForm, currentForm);
		DirectoryForm newDirectoryForm = (DirectoryForm)newForm;
		DirectoryForm currentDirectoryForm = (DirectoryForm)currentForm;
		newDirectoryForm.setParentDirectoryId(currentDirectoryForm.getParentDirectoryId());
		newDirectoryForm.setParentDirectoryName(currentDirectoryForm.getParentDirectoryName());
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Directory directory = (Directory)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			directory.setCreator(sessionInfo.getUserName()); //创建人
			directory.setCreatorId(sessionInfo.getUserId()); //创建人ID
			directory.setCreated(DateTimeUtils.now()); //创建时间
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		DirectoryForm directoryForm = (DirectoryForm)form;
		DirectoryService directoryService = (DirectoryService)getBusinessService(form.getFormDefine());
		//获取配置的权限类型列表
		String[] popedomNames = request.getParameterValues("popedomName");
		if(popedomNames!=null && popedomNames.length>0) {
			List popedomConfigs = new ArrayList();
			for(int i=0; i<popedomNames.length; i++) {
				DirectoryPopedomConfig popedomConfig = new DirectoryPopedomConfig(); 
				popedomConfig.setPopedomName(popedomNames[i]);
				popedomConfig.setUserIds(request.getParameter("popedomUserIds_" + popedomNames[i])); //有权限的用户ID列表
				popedomConfig.setUserNames(request.getParameter("popedomUserNames_" + popedomNames[i])); //有权限的用户姓名列表
				popedomConfigs.add(popedomConfig);
			}
			//保存权限配置
			directoryService.saveDirectoryPopedomConfigs(directory, popedomConfigs, (OPEN_MODE_CREATE.equals(openMode)), sessionInfo);
		}
		//更新引用的目录
		if(request.getParameter("linkedDirectoryNames")!=null) {
			directoryService.updateLinkedDirectories(directory.getId(), directoryForm.getLinkedDirectoryIds(), sessionInfo);
		}
		return record;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		if(record instanceof Directory) {
			//检查上级机构是否是当前机构
			Directory directory = (Directory)record;
			DirectoryForm directoryForm = (DirectoryForm)form;
			if(directory.getParentDirectoryId()==directory.getId() && directory.getId()!=0) {
				directoryForm.setError("上级目录不允许选择当前目录");
				throw new ValidateException();
			}
			//检查是否下级站点或栏目
			DirectoryService directoryService = (DirectoryService)getBusinessService(((ActionForm)form).getFormDefine());
			if(ListUtils.findObjectByProperty(directoryService.listParentDirectories(directory.getParentDirectoryId(), null), "id", new Long(directory.getId()))!=null) {
				directoryForm.setError("上级不允许选择当前目录的下级");
				throw new ValidateException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRefeshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		if("delete".equals(currentAction)) {
			return "try{top.opener.top.document.getElementById(\"navigator\").contentWindow.setTimeout(\"window.tree.deleteNode('" + form.getId() + "')\", 10);}catch(e){}";
		}
		else if("save".equals(currentAction)) {
			Directory directory = (Directory)record;
			if(OPEN_MODE_CREATE.equals(openMode)) { //新目录
				return "try{top.opener.top.document.getElementById(\"navigator\").contentWindow.setTimeout(\"window.tree.reloadChildNodes('" + directory.getParentDirectoryId() + "')\", 10);}catch(e){}";
			}
			else { //更新目录
				return "try{" +
					   " top.opener.top.document.getElementById(\"navigator\").contentWindow.setTimeout(\"window.tree.renameNode('" + directory.getId() + "', '" + directory.getDirectoryName() + "')\", 10);" +
					   " top.opener.top.document.getElementById(\"view\").contentWindow.setTimeout(\"refreshView('viewPackage')\", 100);" +
					   "}" +
					   "catch(e) {" +
					   "}";
			}
		}
		return super.generateRefreshOpenerScript(form, record, openMode, currentAction, actionResult, request, sessionInfo);
	}
}