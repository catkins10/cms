package com.yuanluesoft.wechat.actions.responsemessage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.wechat.forms.ResponseMessage;
import com.yuanluesoft.wechat.pojo.WechatMessageResponse;

/**
 * 
 * @author linchuan
 *
 */
public class ResponseMessageAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			WechatMessageResponse messageResponse = (WechatMessageResponse)record;
			ResponseMessage responseMessageForm = (ResponseMessage)form;
			if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || getOrgService().isOrgManager(OPEN_MODE_CREATE.equals(openMode) ? responseMessageForm.getUnitId() : messageResponse.getUnitId(), sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch (ServiceException e) {
		
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		ResponseMessage responseMessageForm = (ResponseMessage)form;
		if(responseMessageForm.getNews()!=null && responseMessageForm.getNews().size()>=10) {
			responseMessageForm.setAppendNewsDisabled(true);
		}
	}
}