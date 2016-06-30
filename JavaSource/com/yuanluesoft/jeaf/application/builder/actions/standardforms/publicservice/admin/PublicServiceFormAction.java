package com.yuanluesoft.jeaf.application.builder.actions.standardforms.publicservice.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class PublicServiceFormAction extends PublicServiceAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		String[] formName = workflowForm.getFormDefine().getName().split("/");
		return "run" + StringUtils.capitalizeFirstLetter(formName[formName.length-1]);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			String editPrivilege = form.getFormDefine().getExtendedParameter("editPrivilege");
			if(editPrivilege!=null && !acl.contains(editPrivilege)) { //指定编辑权限
				throw new PrivilegeException();
			}
		}
		char accessLevel = RecordControlService.ACCESS_LEVEL_NONE;
		try {
			accessLevel = super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
		}
		catch(PrivilegeException e) {
			
		}
		if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
			return accessLevel;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException();
		}
		String visitPrivilege = form.getFormDefine().getExtendedParameter("visitPrivilege");
		if(visitPrivilege!=null && acl.contains(visitPrivilege)) {
			accessLevel = RecordControlService.ACCESS_LEVEL_READONLY;
		}
		if(accessLevel<=RecordControlService.ACCESS_LEVEL_NONE) {
			throw new PrivilegeException();
		}
		return accessLevel;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		String deletePrivilege = form.getFormDefine().getExtendedParameter("deletePrivilege");
		if(deletePrivilege!=null && acl.contains(deletePrivilege)) { //指定删除权限
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}
}