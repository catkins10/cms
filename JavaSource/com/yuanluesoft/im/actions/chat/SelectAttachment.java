package com.yuanluesoft.im.actions.chat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SelectAttachment extends FormAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeAttachmentAction(mapping, form, request, response);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getTemporaryAttachmentExpiresHours()
	 */
	public int getTemporaryAttachmentExpiresHours() throws Exception {
		IMService imService = (IMService)getService("imService");
		return imService.getAttachmentSaveDays() * 24;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.action.BaseAction#getSessionInfo(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request, HttpServletResponse response) throws SessionException, SystemUnregistException {
		return RequestUtils.getSessionInfo(request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
}