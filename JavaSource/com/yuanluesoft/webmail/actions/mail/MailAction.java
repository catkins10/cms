/*
 * Created on 2005-5-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.actions.mail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.yuanluesoft.webmail.pojo.Mail;
import com.yuanluesoft.webmail.pojo.MailBody;
import com.yuanluesoft.webmail.service.MailboxService;
import com.yuanluesoft.webmail.service.WebMailService;

/**
 * @author root
 *
 */
public class MailAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    WebMailService webMailService = (WebMailService)getService("webMailService");
		com.yuanluesoft.webmail.forms.Mail mailForm = (com.yuanluesoft.webmail.forms.Mail)form;
		fillForm(form, webMailService.createMail(mailForm.getMailTo(), sessionInfo), RecordControlService.ACCESS_LEVEL_EDITABLE, acl, sessionInfo, request, response);
		if(mailForm.getMailTo()!=null && !mailForm.getMailTo().isEmpty()) {
			mailForm.setMailTo(mailForm.getMailTo().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		}
	}
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Mail mail = (Mail)record;
		com.yuanluesoft.webmail.forms.Mail mailForm = (com.yuanluesoft.webmail.forms.Mail)form;
		if(mail.getMailBodies()!=null && !mail.getMailBodies().isEmpty()) {
			mailForm.setHtmlBody(((MailBody)mail.getMailBodies().iterator().next()).getBody());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(id<=0) {
			return null;
		}
		WebMailService webMailService = (WebMailService)getService("webMailService");
		return webMailService.receiveMail(id, true, sessionInfo);
	}
	
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
     */
    public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
        Mail mail = (Mail)record;
        com.yuanluesoft.webmail.forms.Mail mailForm = (com.yuanluesoft.webmail.forms.Mail)form;
		
        //设置正文
		Set mailBodies = new HashSet();
		mail.setMailBodies(mailBodies);
		if(mailForm.getHtmlBody()!=null && !mailForm.getHtmlBody().equals("")) {
			MailBody mailBody = new MailBody();
			mailBody = new MailBody();
			mailBody.setBody(mailForm.getHtmlBody());
			mailBodies.add(mailBody);
		}
		
		//保存邮件
        WebMailService webMailService = (WebMailService)getService("webMailService");
        webMailService.saveMail(mail, sessionInfo, OPEN_MODE_CREATE.equals(openMode));
        return record;
    }
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deletePojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		WebMailService webMailService = (WebMailService)getService("webMailService");
		webMailService.deleteMail(form.getId(), sessionInfo);
	}

	/* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
        //都允许删除
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
        if(OPEN_MODE_CREATE.equals(openMode) || "preview".equals(openMode)) {
            return RecordControlService.ACCESS_LEVEL_EDITABLE;
        }
        Mail pojoMail = (Mail)record;
        //不是收邮件,都可以编辑
        return pojoMail.getMailboxId()<MailboxService.MAILBOX_INBOX_ID ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
    }
}