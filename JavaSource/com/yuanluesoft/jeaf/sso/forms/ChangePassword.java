package com.yuanluesoft.jeaf.sso.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ChangePassword extends ActionForm {
	private String userName; //用户名
	private int passwordStrength; //密码强度
	private String oldPassword; //旧密码
	private String newPassword; //新密码
	
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
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}
	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
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
}