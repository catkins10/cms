package com.yuanluesoft.jeaf.sso.actions.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.sso.forms.LoginForm;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.sso.service.exception.SsoSessionException;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends LoginAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) { //校验登录URL是否正确
    		throw new Exception("url invalid");
    	}
    	LoginForm loginForm = (LoginForm)form;
    	String loginError = loginForm.getLoginError();
    	if(loginError==null || loginError.equals("")) {
    		loginError = loginForm.getLoginReason();
    	}
    	if(!loginForm.isForceLogin() && (loginForm.getLoginError()==null || loginForm.getLoginError().equals(""))) { //不是强制登录
    		try { //获取TICKET
    			String ticket = createTicket(request, response, loginForm.isAnonymousEnable());
    			redirect(loginForm.getRedirect(), ticket, request, response, false);
    			return null;
    		}
    		catch(SsoSessionException se) {
    			if(loginError==null || loginError.equals("")) {
    				loginError = se.getMessage();
    			}
    		}
    	}
    	loginForm.setError(loginError);
    	request.setAttribute("loginError", loginError);
    	//设置登录页面的URL,以便在登录失败时能回到本页面
    	if(loginForm.getLoginPage()==null || loginForm.getLoginPage().equals("")) {
    		loginForm.setLoginPage(RequestUtils.getRequestURL(request, true));
    	}
    	//设置请求代码,防止恶意提交
		FormSecurityService formSecurityService = (FormSecurityService)getService("formSecurityService");
		if(loginForm.getRequestCode()==null || loginForm.getRequestCode().isEmpty()) {
			loginForm.setRequestCode(formSecurityService.registRequest(false));
		}
		else {
			formSecurityService.setValidateCodeImageRequired(loginForm.getRequestCode(), false);
		}
		return mapping.findForward("load");
    }
    
	/**
	 * 创建TICKET
	 * @param request
	 * @param anonymousEnable
	 * @return
	 * @throws SystemUnregistException
	 * @throws Exception
	 */
	private String createTicket(HttpServletRequest request, HttpServletResponse response, boolean anonymousEnable) throws SsoSessionException, SystemUnregistException {
		String clientSessionId = CookieUtils.getCookie(request, "ClientSessionId"); //客户端登录COOKIE
		if(clientSessionId!=null) {
			CookieUtils.removeCookie(response, "ClientSessionId", "/", null);
		}
		String ssoSessionId = CookieUtils.getCookie(request, SsoSessionService.SSO_SESSION_COOKIE_NAME);
		SsoSessionService ssoSessionService = (SsoSessionService)getService("ssoSessionService");
		if(ssoSessionId!=null && !"".equals(ssoSessionId)) { //有会话ID
			try {
				return ssoSessionService.createTicket(ssoSessionId);
			}
			catch(SsoSessionException se) {
				if(clientSessionId==null || "".equals(clientSessionId)) {
					throw se;
				}
			}
		}
		else if(clientSessionId==null || "".equals(clientSessionId)) {
			if(anonymousEnable) { //没有会话ID,且允许匿名访问
				return "anonymous"; 
			}
			throw new SsoSessionException(null);
		}
		ssoSessionId = ssoSessionService.cloneSsoSession(clientSessionId);
		CookieUtils.addCookie(response, SsoSessionService.SSO_SESSION_COOKIE_NAME, ssoSessionId, -1, "/", null, null);
		return ssoSessionService.createTicket(ssoSessionId);
	}
}