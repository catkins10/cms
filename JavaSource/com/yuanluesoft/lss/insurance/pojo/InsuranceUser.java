package com.yuanluesoft.lss.insurance.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 养老保险:用户(insurance_user)
 * @author linchuan
 *
 */
public class InsuranceUser extends Record {
	private String identityCardNumber; //身份证号码
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
}