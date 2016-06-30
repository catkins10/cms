package com.yuanluesoft.webmail.actions.mailbox;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.webmail.forms.Mailbox;
import com.yuanluesoft.webmail.service.MailboxService;

/**
 * 
 * @author linchuan
 *
 */
public class MailboxAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deletePojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		MailboxService mailboxService = (MailboxService)getService("mailboxService");
		mailboxService.deleteMailbox(form.getId(), sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		MailboxService mailboxService = (MailboxService)getService("mailboxService");
		return mailboxService.loadMailbox(form.getId(), sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		MailboxService mailboxService = (MailboxService)getService("mailboxService");
		Mailbox mailboxForm = (Mailbox)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			mailboxService.addMailbox(mailboxForm.getMailboxName(), sessionInfo);
		}
		else {
			mailboxService.renameMailbox(mailboxForm.getId(), mailboxForm.getMailboxName(), sessionInfo);
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Mailbox mailboxForm = (Mailbox)form;
		if(mailboxForm.getId()<=20 && !OPEN_MODE_CREATE.equals(openMode)) {
			mailboxForm.getFormActions().removeFormAction("保存");
			mailboxForm.getFormActions().removeFormAction("删除");
		}
	}

	/**
	 * 刷新视图
	 * @param response
	 */
	protected void refleshMailboxView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String html = "<html>" +
					  " <head>" +
					  "  <script language=\"JavaScript\" charset=\"utf-8\" src=\"" + request.getContextPath() + "/jeaf/common/js/common.js\"></script>" +
					  " </head>" +
					  " <body onload=\"DialogUtils.getDialogOpener().location.reload();DialogUtils.closeDialog();\"></body>" +
					  "</html>";
		response.getWriter().write(html);
	}
}