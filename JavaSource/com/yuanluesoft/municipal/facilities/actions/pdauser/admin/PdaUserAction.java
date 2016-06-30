package com.yuanluesoft.municipal.facilities.actions.pdauser.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.municipal.facilities.forms.admin.PdaUser;

/**
 * 
 * @author linchuan
 *
 */
public class PdaUserAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		super.inheritProperties(newForm, currentForm);
		PdaUser newPdaUserForm = (PdaUser)newForm;
		PdaUser currentPdaUserForm = (PdaUser)currentForm;
		newPdaUserForm.setOrgId(currentPdaUserForm.getOrgId());
		newPdaUserForm.setOrgName(currentPdaUserForm.getOrgName());
	}
}