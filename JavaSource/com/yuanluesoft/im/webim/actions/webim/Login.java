package com.yuanluesoft.im.webim.actions.webim;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * WEBIM登录
 * @author linchuan
 *
 */
public class Login extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//会话检查
    	try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	response.getWriter().print("<html><body onload=\"parent.location.reload()\"></body></html>");
    	return null;
    }
}