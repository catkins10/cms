package com.yuanluesoft.jeaf.sso.actions.changepassword;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sso.service.SsoSessionService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends ChangePasswordAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	anonymousAlways = request.getSession().getAttribute(SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME)!=null;
    	return executeLoadAction(mapping, form, request, response);
    }
}