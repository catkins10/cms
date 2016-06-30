package com.yuanluesoft.webmail.actions.mail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.webmail.service.WebMailService;

/**
 * 
 * @author linchuan
 *
 */
public class Reply extends MailAction {
       
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeLoadAction(mapping, form, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.actions.mail.MailAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WebMailService webMailService = (WebMailService)getService("webMailService");
        com.yuanluesoft.webmail.pojo.Mail mail = webMailService.replyMail(request.getParameter("mailId"), sessionInfo);
        form.setAct("create");
        fillForm(form, mail, RecordControlService.ACCESS_LEVEL_EDITABLE, acl, sessionInfo, request, response);
	}
}