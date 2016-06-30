package com.yuanluesoft.jeaf.usermanage.security.actions.admin.userpasswordpolicy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.security.pojo.UserPasswordPolicy;
import com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService;

/**
 * 
 * @author chuan
 *
 */
public class UserPasswordPolicyAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		UserPasswordPolicy userPasswordPolicy = (UserPasswordPolicy)record;
		com.yuanluesoft.jeaf.usermanage.security.forms.admin.UserPasswordPolicy userPasswordPolicyForm = (com.yuanluesoft.jeaf.usermanage.security.forms.admin.UserPasswordPolicy)form;
		try {
			if(getOrgService().isOrgManager(userPasswordPolicy==null ? userPasswordPolicyForm.getOrgId() : userPasswordPolicy.getOrgId(), sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch (ServiceException e) {
			
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE.equals(getOpenMode(form, request))) {
			com.yuanluesoft.jeaf.usermanage.security.forms.admin.UserPasswordPolicy userPasswordPolicyForm = (com.yuanluesoft.jeaf.usermanage.security.forms.admin.UserPasswordPolicy)form;
			UserSecurityService userSecurityService = (UserSecurityService)getService("userSecurityService");
			return userSecurityService.getUserPasswordPolicy(userPasswordPolicyForm.getOrgId());
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}

}