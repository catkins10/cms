package com.yuanluesoft.jeaf.sessionmanage.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class ResetSession extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
        	getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        return mapping.findForward("logined");
    }
}