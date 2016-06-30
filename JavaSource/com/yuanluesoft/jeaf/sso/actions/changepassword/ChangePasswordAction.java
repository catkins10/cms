package com.yuanluesoft.jeaf.sso.actions.changepassword;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.exception.WrongPasswordException;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.sso.forms.ChangePassword;
import com.yuanluesoft.jeaf.sso.service.SsoSessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.security.exception.PasswordStrengthException;
import com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService;
import com.yuanluesoft.jeaf.util.CookieUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ChangePasswordAction extends DialogFormAction {

	public ChangePasswordAction() {
		super();
		isSecureAction = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		ChangePassword changePasswordForm = (ChangePassword)form;
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		if(sessionInfo==null || sessionInfo.isAnonymous()) {
			sessionInfo = memberServiceList.createSessionInfo((String)request.getSession().getAttribute(SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME));
		}
		if(sessionInfo==null || sessionInfo.isAnonymous()) {
			return;
		}
		changePasswordForm.setUserName(sessionInfo.getUserName()); //用户名
		//设置密码强度
		if(changePasswordForm.getPasswordStrength()==0) {
			UserSecurityService userSecurityService = (UserSecurityService)getService("userSecurityService");
			changePasswordForm.setPasswordStrength(userSecurityService.getPasswordStrength(sessionInfo.getLoginName(), request));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		ChangePassword changePasswordForm = (ChangePassword)form;
		if(sessionInfo==null || sessionInfo.isAnonymous()) {
			sessionInfo = memberServiceList.createSessionInfo((String)request.getSession().getAttribute(SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME));
		}
		UserSecurityService userSecurityService = (UserSecurityService)getService("userSecurityService");
        try {
        	userSecurityService.validatePasswordStrength(sessionInfo.getLoginName(), changePasswordForm.getNewPassword(), request);
        }
    	catch(PasswordStrengthException e) {
    		changePasswordForm.setError("密码安全等级不够");
    		throw new ValidateException();
		}
    	try {
    		memberServiceList.changePassword(sessionInfo.getLoginName(), changePasswordForm.getOldPassword(), changePasswordForm.getNewPassword(), true);
    	}
    	catch(WrongPasswordException e) {
    		changePasswordForm.setError("原密码错误");
			throw new ValidateException();
    	}
    	catch(Exception e) {
    		Logger.exception(e);
    		changePasswordForm.setError("密码修改失败");
			throw new ValidateException();
    	}
    	userSecurityService.passwordChanged(sessionInfo.getUserId());
    	SessionService sessionService = (SessionService)getService("sessionService");
    	sessionService.removeSessionInfo(sessionInfo.getLoginName());
    	//创建新的SSO会话
    	SsoSessionService ssoSessionService = (SsoSessionService)getService("ssoSessionService");
		String ssoSessionId = ssoSessionService.createSsoSession(sessionInfo.getLoginName(), false);
		CookieUtils.addCookie(response, SsoSessionService.SSO_SESSION_COOKIE_NAME, ssoSessionId, -1, "/", null, null);
		request.getSession().removeAttribute(SsoSessionService.SSO_LOGIN_LAST_LOGIN_NAME);
	}
}