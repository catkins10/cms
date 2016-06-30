/*
 * Created on 2006-5-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.actions.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.webmail.forms.WebmailViewForm;
import com.yuanluesoft.webmail.pojo.Mailbox;
import com.yuanluesoft.webmail.service.MailboxService;

/**
 *
 * @author linchuan
 *
 */
public class WebmailViewAction extends ViewFormAction {
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "webmail";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		WebmailViewForm webmailViewForm = (WebmailViewForm)viewForm;
		return webmailViewForm.getViewName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		WebmailViewForm webmailViewForm = (WebmailViewForm)viewForm;
        if("inbox".equals(webmailViewForm.getViewName())) {
	        view.addWhere("Mail.mailboxId=" + webmailViewForm.getMailboxId());
	        if(webmailViewForm.getMailboxId()==MailboxService.MAILBOX_RECYCLE_ID) { //回收站,清除删除操作
	            ListUtils.removeObjectByProperty(view.getActions(), "title", "删除");
	        }
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		WebmailViewForm webmailViewForm = (WebmailViewForm)viewForm;
		if("inbox".equals(webmailViewForm.getViewName())) {
			//重设当前位置
			MailboxService mailboxService = (MailboxService)getService("mailboxService");
			Mailbox mailbox = mailboxService.loadMailbox(webmailViewForm.getMailboxId(), sessionInfo);
			location.clear();
			location.add("电子邮件");
			location.add(mailbox.getMailboxName());
		}
	}
}