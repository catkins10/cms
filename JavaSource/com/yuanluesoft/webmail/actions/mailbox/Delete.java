package com.yuanluesoft.webmail.actions.mailbox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends MailboxAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeDeleteAction(mapping, form, request, response, null, null);
    	if("result".equals(forward.getName())) {
    		refleshMailboxView(request, response);
    		return null;
    	}
    	return forward;
    }
}