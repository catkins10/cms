package com.yuanluesoft.webmail.actions.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.webmail.forms.Mail;
import com.yuanluesoft.webmail.service.WebMailService;

/**
 * 
 * @author linchuan
 *
 */
public class Forward extends MailAction {
       
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeLoadAction(mapping, form, request, response);
    }

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo = getSessionInfo(request, response);
		Mail mailForm = (Mail)form;
		WebMailService webMailService = (WebMailService)getService("webMailService");
		com.yuanluesoft.webmail.pojo.Mail mail = webMailService.forwardMail(Long.parseLong(request.getParameter("mailId")), sessionInfo);
		mailForm.setId(mail.getId());
		mailForm.setAct("edit");
		return super.load(form, request, response);
	}
}