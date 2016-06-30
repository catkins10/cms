package com.yuanluesoft.jeaf.sso.actions.login;

import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.form.service.FormSecurityService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.LoginException;
import com.yuanluesoft.jeaf.membermanage.model.MemberLogin;
import com.yuanluesoft.jeaf.sso.forms.LoginForm;
import com.yuanluesoft.jeaf.sso.matcher.MD5PasswordMatcher;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.usermanage.member.service.MemberService;
import com.yuanluesoft.jeaf.usermanage.security.exception.PasswordOverdueException;
import com.yuanluesoft.jeaf.usermanage.security.exception.PasswordStrengthException;
import com.yuanluesoft.jeaf.usermanage.security.exception.UserHaltException;
import com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService;
import com.yuanluesoft.jeaf.util.CookieUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 
 * @author linchuan
 *
 */
public class Login extends LoginAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) { //校验登录URL是否正确
    		throw new Exception("url invalid");
    	}
    	LoginForm loginForm = (LoginForm)form;
    	if(request.getParameter("rememberUserName")==null) { //参数中没有记住用户名
    		loginForm.setRememberUserName(!"false".equals(CookieUtils.getCookie(request, SsoSessionService.SSO_LOGIN_REMEMBER_LOGIN_NAME)));
    	}
    	SsoSessionService ssoSessionService = (SsoSessionService)getService("ssoSessionService");
    	String loginError = null;
    	ValidateCodeService validateCodeService = (ValidateCodeService)getService("validateCodeService");
    	FormSecurityService formSecurityService = (FormSecurityService)getService("formSecurityService");
    	if(loginForm.getPassword()!=null && !loginForm.getPassword().isEmpty()) { //使用密码登录
	    	if(!formSecurityService.validateRequest(loginForm.getRequestCode())) {
	    		loginForm.setRequestCode(formSecurityService.registRequest(false)); //重新生成请求代码
	    		loginError = "无效请求";
			}
	    	else {
		    	if(formSecurityService.isValidateCodeImageRequired(loginForm.getRequestCode()) && //必须校验验证码
		    	   !validateCodeService.validateCode(request.getParameter("validateCode"), request)) { //校验验证码
		    		loginError = "验证码输入错误";
		    	}
		    	formSecurityService.removeRequest(loginForm.getRequestCode());
		    	loginForm.setRequestCode(null);
	    	}
    	}
    	String changePasswordReason = null;
    	if(loginError==null) {
	    	try {
	    		MemberLogin memberLogin = ssoSessionService.login(loginForm.getUserName(), loginForm.getPassword(), new MD5PasswordMatcher(request), request);
	    		//用户密码校验
	    		UserSecurityService userSecurityService = (UserSecurityService)getService("userSecurityService");
	    		if(memberLogin.isPasswordWrong()) { //密码错误
	    			userSecurityService.passwordWrong(memberLogin.getMemberLoginName(), request);
	    			throw new LoginException(MemberService.LOGIN_INVALID_USERNAME_OR_PASSWORD);
	    		}
	    		//密码没有错误
	    		if(memberLogin.getPassword()!=null) { //密码不为空
	    			userSecurityService.loginAudit(memberLogin.getMemberLoginName(), request); //检查密码是否过期,或者强度不足
	    		}
	    		//获取原来的会话ID
		    	String ssoSessionId = CookieUtils.getCookie(request, SsoSessionService.SSO_SESSION_COOKIE_NAME);
	    		boolean firstLogin = (ssoSessionId==null || ssoSessionId.equals(""));
	    		if(!firstLogin) {
	    			try {
	    				ssoSessionService.removeSsoSession(ssoSessionId); //原先登录过,则删除原来的会话
	    			}
	    			catch(Exception e) {
	    				Logger.exception(e);
	    			}
	    		}
	    		//创建新的SSO会话
	    		ssoSessionId = ssoSessionService.createSsoSession(memberLogin.getMemberLoginName(), false);
	    		//设置最后登录者Cookie
	    		saveLastLoginUserName(loginForm, response);
	        	CookieUtils.addCookie(response, SsoSessionService.SSO_SESSION_COOKIE_NAME, ssoSessionId, -1, "/", null, null);
	        	validateCodeService.cleanCode(request, response); //清理验证码
	        	//重定向
	        	String redirect = loginForm.getRedirect()==null ? loginForm.getLoginPage() : loginForm.getRedirect(); //重定向地址
	        	redirect(redirect, ssoSessionService.createTicket(ssoSessionId), request, response, (firstLogin && loginForm.isFullScreen())); //第一次登录时，全屏
	        	return null;
	     	}
	    	catch(PasswordStrengthException e) {
	    		request.getSession().setAttribute(SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME, loginForm.getUserName());
	    		changePasswordReason = "密码安全等级过低，请立即修改！";
	    	}
	    	catch(PasswordOverdueException e) {
	    		request.getSession().setAttribute(SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME, loginForm.getUserName());
	    		changePasswordReason = "密码已过期，请立即修改！";
	    	}
	    	catch(Exception e) {
	    		loginError = e instanceof LoginException ? e.getMessage() : (e instanceof UserHaltException ? MemberService.LOGIN_ACCOUNT_IS_HALT : null);
	    		if(loginError==null || loginError.isEmpty()) {
	    			e.printStackTrace();
	    			loginError = "出错，登录不能完成";
	    		}
	    		validateCodeService.cleanCode(request, response);
	    	}
    	}
    	if(loginForm.getLoginPage()==null) {
    		loginForm.setError(loginError);
    		loginForm.setChangePasswordReason(changePasswordReason);
    		return mapping.getInputForward();
    	}
		//重定向到登录页面
		String loginPage = StringUtils.removeQueryParameters(loginForm.getLoginPage(), "loginError"); //删除URL中的loginError
		loginPage = StringUtils.removeQueryParameters(loginPage, "loginReason"); //删除URL中的loginReason
		loginPage = StringUtils.removeQueryParameters(loginPage, "changePasswordReason"); //删除URL中的changePasswordReason
		loginPage = StringUtils.removeQueryParameters(loginPage, "userName"); //删除URL中的userName
		if(RequestUtils.isHackerURL(loginPage)) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return null;
		}
		//把错误提示加入到URL中
		String encoding = loginForm.getEncoding()==null || loginForm.getEncoding().equals("") ? "utf-8" : loginForm.getEncoding();
		loginPage += (loginPage.indexOf('?')==-1 ? "?" : "&") +
					 "userName=" + URLEncoder.encode(loginForm.getUserName(), encoding) +
					 "&rememberUserName=" + loginForm.isRememberUserName() +
					 (loginError==null ? "" : "&loginError=" + URLEncoder.encode(loginError, encoding)) +
					 (changePasswordReason==null ? "" : "&changePasswordReason=" + URLEncoder.encode(changePasswordReason, encoding)) +
					 "&displayMode=" + URLEncoder.encode(loginForm.getDisplayMode(), encoding);
		PrintWriter writer = response.getWriter();
		writer.println("<html><script>");
		writer.println("window.location.replace('" + RequestUtils.resetRedirectURL(loginPage) + "');");
		writer.println("</script></html>");
		return null;
    }
	
	/**
	 * 保存最后登录人到cookie
	 * @param loginForm
	 * @param response
	 */
    private void saveLastLoginUserName(LoginForm loginForm, HttpServletResponse response) {
		try {
			CookieUtils.addCookie(response, SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME, URLEncoder.encode(loginForm.getUserName(), "utf-8"), loginForm.isRememberUserName() ? 0xffffff : -1, "/", null, null);
			CookieUtils.addCookie(response, SsoSessionService.SSO_LOGIN_REMEMBER_LOGIN_NAME, "" + loginForm.isRememberUserName(), 0xffffff, "/", null, null);
		}
		catch(Exception e) {
			
		}
	}
}