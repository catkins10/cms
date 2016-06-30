package com.yuanluesoft.jeaf.sso.actions.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sso.forms.LoginForm;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.util.CookieUtils;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class Logout extends LoginAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) { //校验登录URL是否正确
    		throw new Exception("url invalid");
    	}
    	LoginForm loginForm = (LoginForm)form;
    	String ssoSessionId = CookieUtils.getCookie(request, SsoSessionService.SSO_SESSION_COOKIE_NAME);
    	SsoSessionService ssoSessionService = (SsoSessionService)getService("ssoSessionService");
    	ssoSessionService.removeSsoSession(ssoSessionId);
    	CookieUtils.removeCookie(response, SsoSessionService.SSO_SESSION_COOKIE_NAME, "/", null);
    	redirect(loginForm.getRedirect(), "anonymous", request, response, false);
    	return null;
    }
}