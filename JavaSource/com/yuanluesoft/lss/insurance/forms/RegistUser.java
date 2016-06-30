package com.yuanluesoft.lss.insurance.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class RegistUser extends ActionForm {
	private String identityCardNumber; //身份证号码
	private String name; //姓名
	private String password; //密码
	
	/**
	 * @return the identityCardNumber
	 */
	public String getIdentityCardNumber() {
		return identityCardNumber;
	}
	/**
	 * @param identityCardNumber the identityCardNumber to set
	 */
	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}