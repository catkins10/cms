package com.yuanluesoft.jeaf.messagecenter.actions.messenger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.messagecenter.forms.MessengerLogin;
import com.yuanluesoft.jeaf.sso.matcher.PlainPasswordMatcher;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class Login extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	MessengerLogin formMessengerLogin = (MessengerLogin)form;
    	SessionService sessionService = (SessionService)getService("sessionService");
    	SsoSessionService ssoSessionService = (SsoSessionService)getService("ssoSessionService");
    	try {
    		String loginName = ssoSessionService.login(formMessengerLogin.getLoginName(), formMessengerLogin.getPassword(), new PlainPasswordMatcher(), request).getMemberLoginName();
    		SessionInfo sessionInfo = sessionService.getSessionInfo(loginName);
        	//保存会话信息
        	request.getSession().setAttribute("SessionInfo", sessionInfo);
        	CookieUtils.addCookie(response, "JSESSIONID", request.getSession().getId(), -1, "/", null, null);
        	response.getWriter().write("OK");
    	}
    	catch(Exception e) {
    		response.getWriter().write("FAILD");
        }
    	return null;
    }
}