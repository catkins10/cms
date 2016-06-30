package com.yuanluesoft.bidding.enterprise.forms;

/**
 * 
 * @author chuan
 *
 */
public class Enterprise extends com.yuanluesoft.bidding.enterprise.forms.admin.Enterprise {
	private String loginName; //登录用户名
	private String password; //密码
	
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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