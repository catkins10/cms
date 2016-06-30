package com.yuanluesoft.webmail.actions.mailfilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.webmail.actions.view.WebmailViewAction;
import com.yuanluesoft.webmail.service.MailFilterService;

/**
 * 
 * @author linchuan
 *
 */
public class AdjustFilterPriority extends WebmailViewAction {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
	    String action = request.getParameter("action");
        if(action!=null) {
	        MailFilterService mailFilterService = (MailFilterService)getService("mailFilterService");
	        mailFilterService.adjustFilterPriority(viewForm.getViewPackage().getSelectedIds(), "up".equals(action), sessionInfo);
	    }
        return mapping.getInputForward();
    }
}