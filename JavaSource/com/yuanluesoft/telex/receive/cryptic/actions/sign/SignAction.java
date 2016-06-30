package com.yuanluesoft.telex.receive.cryptic.actions.sign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.telex.receive.base.model.TelegramSignPerson;

/**
 * 
 * @author linchuan
 *
 */
public class SignAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!acl.contains("application_manager") && !acl.contains("manageUnit_sign")) { //没有经办人
			throw new PrivilegeException();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#loadFormResource(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void loadFormResource(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.loadFormResource(form, acl, sessionInfo, request);
		form.setDisplayMode("dialog");
	}

	/**
	 * 设置当前签收用户
	 * @param request
	 * @param signPersonId
	 */
	protected void setCurrentSignPerson(HttpServletRequest request, TelegramSignPerson signPerson) {
		request.getSession().setAttribute("CurrentSignPerson", signPerson);
	}
	
	/**
	 * 获取当前签收用户
	 * @param request
	 * @return
	 */
	protected TelegramSignPerson getCurrentSignPerson(HttpServletRequest request) {
		return (TelegramSignPerson)request.getSession().getAttribute("CurrentSignPerson");
	}
}