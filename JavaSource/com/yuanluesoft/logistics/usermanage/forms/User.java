package com.yuanluesoft.logistics.usermanage.forms;


/**
 * 
 * @author linchuan
 *
 */
public class User extends com.yuanluesoft.logistics.usermanage.forms.admin.User {
	private String passwordConfirm; //登录密码确认

	/**
	 * @return the passwordConfirm
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	/**
	 * @param passwordConfirm the passwordConfirm to set
	 */
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}	
}