package com.yuanluesoft.jeaf.sso.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author chuan
 *
 */
public class ForgetPassword extends ActionForm {
	private String userName; //用户名
	private String mailAddress; //邮件地址
	private String code; //密码修改验证码
	private int passwordStrength; //密码强度
	private String newPassword; //新密码
	
	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the passwordStrength
	 */
	public int getPasswordStrength() {
		return passwordStrength;
	}
	/**
	 * @param passwordStrength the passwordStrength to set
	 */
	public void setPasswordStrength(int passwordStrength) {
		this.passwordStrength = passwordStrength;
	}
	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}
	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
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
}