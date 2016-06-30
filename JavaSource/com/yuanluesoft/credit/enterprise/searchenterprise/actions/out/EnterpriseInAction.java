package com.yuanluesoft.credit.enterprise.searchenterprise.actions.out;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.credit.enterprise.searchenterprise.forms.EnterpriseOut;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author zyh
 *
 */
public class EnterpriseInAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}
	
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		EnterpriseOut enterpriseForm = (EnterpriseOut)form;
		if(enterpriseForm.getPassword()!=null &&
				   !"".equals(enterpriseForm.getPassword()) &&
				   (!enterpriseForm.getPassword().startsWith("{") ||
				    !enterpriseForm.getPassword().endsWith("}"))) {
			enterpriseForm.setPassword("{" + enterpriseForm.getPassword() + "}");
				}
	}
	
	
}