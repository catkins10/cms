package com.yuanluesoft.fdi.project.actions.project.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.fdi.industry.service.FdiIndustryService;
import com.yuanluesoft.fdi.project.forms.admin.Project;
import com.yuanluesoft.fdi.project.pojo.FdiProject;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Project projectForm = (Project)form;
		FdiProject project = (FdiProject)record;
		long industryId = projectForm.getIndustryId();
		if(industryId==0 && project!=null) {
			industryId = project.getIndustryId();
		}
		if(industryId==0) { //新记录,不做权限控制
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		//检查用户对行业的权限
		FdiIndustryService fdiIndustryService = (FdiIndustryService)getService("fdiIndustryService");
		char level;
		try {
			level = fdiIndustryService.getIndustryAccessLevel(industryId + "", sessionInfo);
		}
		catch (ServiceException e) {
			throw new PrivilegeException(e);
		}
		if(level>RecordControlService.ACCESS_LEVEL_READONLY) { //有编辑权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(level>=RecordControlService.ACCESS_LEVEL_READONLY || acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //有查询权限或者是管理员
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		form.getTabs().addTab(-1, "pushs", "推进情况", "pushs.jsp", false);
	}
}