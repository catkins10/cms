package com.yuanluesoft.jeaf.sessionmanage.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class Logout extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        externalAction = "true".equals(request.getParameter("external"));
    	return redirectToLogout(mapping, form, request.getParameter("redirect"), request, response);
    }
}