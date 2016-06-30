package com.yuanluesoft.fet.project.actions.admin.project;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.fet.project.forms.admin.Project;
import com.yuanluesoft.fet.project.pojo.FetProject;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ProjectAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) { //管理员有权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		//TODO:活动的管理员有对属于本活动的项目的管理权
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Project projectForm = (Project)form;
		//设置TAB列表
		projectForm.getTabs().addTab(-1, "basic", "基本信息", null, true);
		projectForm.getTabs().addTab(-1, "signInvestment", "签约投资情况", null, false);
		projectForm.getTabs().addTab(-1, "investment", "资金到达情况", null, false);
		projectForm.getTabs().addTab(-1, "submit", "报批情况", null, false);
		projectForm.getTabs().addTab(-1, "building", "开竣工情况", null, false);
		projectForm.getTabs().addTab(-1, "problemtab", "存在的问题", null, false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			FetProject project = (FetProject)record;
			project.setCreated(DateTimeUtils.now());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}
