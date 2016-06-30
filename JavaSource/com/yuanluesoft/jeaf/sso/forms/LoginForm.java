package com.yuanluesoft.jeaf.sso.forms;

import java.net.URLEncoder;

import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class LoginForm extends com.yuanluesoft.jeaf.form.ActionForm {
	private String redirect; //重定向的URL
	private String loginPage; //登录页面的URL
	private boolean anonymousEnable; //是否允许匿名访问
	private boolean forceLogin; //是否强制登录,如:没有访问权限、会话类型不匹配
	private String loginReason; //登录原因
	private String loginError; //登录错误
	private String changePasswordReason; //修改密码的原因
	private String encoding; //loginPageURL的编码方式,默认utf8
	private boolean fullScreen; //是否全屏

	private String userName; //用户名
	private String password; //密码
	private boolean rememberUserName = true; //是否记住用户名
	
	/**
	 * 获取修改密码脚本
	 * @return
	 */
	public String getChangePasswordScript() {
		if(changePasswordReason==null) {
			return null;
		}
		try {
			String url = Environment.getWebApplicationSafeUrl() + "/jeaf/sso/changePassword.shtml" +
						 "?prompt=" + URLEncoder.encode(changePasswordReason, "utf-8") +
						 "&loginName=" + URLEncoder.encode(userName, "utf-8");
			String script = "window.onload = function() {" +
							"	DialogUtils.openDialog('" + url + "', 430, 260);" +
							"};";
			return script;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * @return the anonymousEnable
	 */
	public boolean isAnonymousEnable() {
		return anonymousEnable;
	}
	/**
	 * @param anonymousEnable the anonymousEnable to set
	 */
	public void setAnonymousEnable(boolean anonymousEnable) {
		this.anonymousEnable = anonymousEnable;
	}
	/**
	 * @return the forceLogin
	 */
	public boolean isForceLogin() {
		return forceLogin;
	}
	/**
	 * @param forceLogin the forceLogin to set
	 */
	public void setForceLogin(boolean forceLogin) {
		this.forceLogin = forceLogin;
	}
	/**
	 * @return the fullScreen
	 */
	public boolean isFullScreen() {
		return fullScreen;
	}
	/**
	 * @param fullScreen the fullScreen to set
	 */
	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}
	/**
	 * @return the loginPage
	 */
	public String getLoginPage() {
		return loginPage;
	}
	/**
	 * @param loginPage the loginPage to set
	 */
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
	/**
	 * @return the loginReason
	 */
	public String getLoginReason() {
		return loginReason;
	}
	/**
	 * @param loginReason the loginReason to set
	 */
	public void setLoginReason(String loginReason) {
		this.loginReason = loginReason;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the redirect
	 */
	public String getRedirect() {
		return redirect;
	}
	/**
	 * @param redirect the redirect to set
	 */
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the loginError
	 */
	public String getLoginError() {
		return loginError;
	}
	/**
	 * @param loginError the loginError to set
	 */
	public void setLoginError(String loginError) {
		this.loginError = loginError;
	}
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	/**
	 * @return the changePasswordReason
	 */
	public String getChangePasswordReason() {
		return changePasswordReason;
	}
	/**
	 * @param changePasswordReason the changePasswordReason to set
	 */
	public void setChangePasswordReason(String changePasswordReason) {
		this.changePasswordReason = changePasswordReason;
	}


	/**
	 * @return the rememberUserName
	 */
	public boolean isRememberUserName() {
		return rememberUserName;
	}


	/**
	 * @param rememberUserName the rememberUserName to set
	 */
	public void setRememberUserName(boolean rememberUserName) {
		this.rememberUserName = rememberUserName;
	}
}