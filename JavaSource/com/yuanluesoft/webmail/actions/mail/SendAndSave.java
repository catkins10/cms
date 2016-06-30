package com.yuanluesoft.webmail.actions.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.webmail.service.MailboxService;
import com.yuanluesoft.webmail.service.WebMailService;

/**
 * 
 * @author linchuan
 *
 */
public class SendAndSave extends MailAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSaveAction(mapping, form, request, response, false, null, "发送成功！", null);
    }

    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.actions.mail.MailAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		WebMailService webMailService = (WebMailService)getService("webMailService");
		com.yuanluesoft.webmail.pojo.Mail mail = (com.yuanluesoft.webmail.pojo.Mail)record;
		webMailService.moveMail(form.getId(), MailboxService.MAILBOX_OUTBOX_ID, sessionInfo);
		webMailService.sendMail(mail, sessionInfo);
		return record;
    }
}