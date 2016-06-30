package com.yuanluesoft.jeaf.sso.actions.forgetpassword;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.forms.ForgetPassword;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.security.exception.PasswordStrengthException;
import com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService;

/**
 * 
 * @author linchuan
 *
 */
public class ForgetPasswordAction extends DialogFormAction {

	public ForgetPasswordAction() {
		super();
		isSecureAction = true;
		anonymousAlways = true;
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
		if(form.getAct()==null || form.getAct().isEmpty()) {
			form.setAct("inputLoginName");
		}
		if("inputLoginName".equals(form.getAct())) {
			form.getFormActions().removeFormAction("确定");
		}
		else {
			form.getFormActions().removeFormAction("下一步");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		ForgetPassword forgetPasswordForm = (ForgetPassword)form;
		UserSecurityService userSecurityService = (UserSecurityService)getService("userSecurityService");
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		sessionInfo = memberServiceList.createSessionInfo(forgetPasswordForm.getUserName());
		if("inputLoginName".equals(forgetPasswordForm.getAct())) { //输入用户名
			Member member;
			if(sessionInfo==null || sessionInfo.isAnonymous()) {
				forgetPasswordForm.setError("登录用户名不存在");
			}
			else if((member = memberServiceList.getMember(sessionInfo.getUserId())).getMailAddress()==null || member.getMailAddress().isEmpty()) {
				forgetPasswordForm.setError("邮箱未设置，请联系管理员修改密码");
			}
			else {
				boolean success = false;
				try {
					userSecurityService.sendResetMail(sessionInfo.getLoginName(), member.getMailAddress(), request); //生成验证码并发送邮件
					success = true;
				}
				catch(Exception e) {
					Logger.exception(e);
					forgetPasswordForm.setError("密码重置邮件发送失败，请联系管理员修改密码");
				}
				if(success) {
					forgetPasswordForm.setAct("inputNewPassword"); //状态设为输入新密码
					forgetPasswordForm.setMailAddress(member.getMailAddress()); //设置邮箱地址
					forgetPasswordForm.setPasswordStrength(userSecurityService.getPasswordStrength(sessionInfo.getLoginName(), request)); //设置密码强度
				}
			}
		}
		else if("inputNewPassword".equals(forgetPasswordForm.getAct())) { //输入新密码
			try {
				userSecurityService.validatePasswordStrength(sessionInfo.getLoginName(), forgetPasswordForm.getNewPassword(), request);
			}
			catch(PasswordStrengthException e) {
				forgetPasswordForm.setError("密码安全等级不够");
				throw new ValidateException();
			}
			if(!userSecurityService.validateResetCode(sessionInfo.getLoginName(), forgetPasswordForm.getCode(), request)) {
				forgetPasswordForm.setError("重置验证码错误或者已经失效");
				throw new ValidateException();
			}
			boolean success = false;
	    	try {
	    		success = memberServiceList.changePassword(forgetPasswordForm.getUserName(), null, forgetPasswordForm.getNewPassword(), false);
	    		userSecurityService.passwordChanged(sessionInfo.getUserId());
	    	}
	    	catch(Exception e) {
	    		Logger.exception(e);
	    	}
	    	if(!success) {
	    		forgetPasswordForm.setError("密码修改失败");
				throw new ValidateException();
	    	}
		}
	}
}