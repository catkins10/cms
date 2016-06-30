package com.yuanluesoft.jeaf.messagecenter.actions.sendmessage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 发送消息
 * @author linchuan
 *
 */
public class SendMessageAction extends DialogFormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			if(getAccessControlService().getLinkAcl("sendMessage", sessionInfo).isEmpty()) {
				throw new PrivilegeException();
			}
		}
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
	}
}