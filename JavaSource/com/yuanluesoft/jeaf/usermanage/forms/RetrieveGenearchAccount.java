package com.yuanluesoft.jeaf.usermanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 *
 */
public class RetrieveGenearchAccount extends ActionForm {
	private long classId;
    private String classFullName;
    private String childName;
    private String name;
	private String mobile;
	private String validateCode;
	
	//开通成功后的返回值
	private String loginName; //用户名
	private String password; //密码
	
	private String newLoginName; //新的用户名
	private String newPassword; //新密码
	
	/**
	 * @return the childName
	 */
	public String getChildName() {
		return childName;
	}
	/**
	 * @param childName the childName to set
	 */
	public void setChildName(String childName) {
		this.childName = childName;
	}
	/**
	 * @return the classFullName
	 */
	public String getClassFullName() {
		return classFullName;
	}
	/**
	 * @param classFullName the classFullName to set
	 */
	public void setClassFullName(String classFullName) {
		this.classFullName = classFullName;
	}
	/**
	 * @return the classId
	 */
	public long getClassId() {
		return classId;
	}
	/**
	 * @param classId the classId to set
	 */
	public void setClassId(long classId) {
		this.classId = classId;
	}
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	/**
	 * @return the validateCode
	 */
	public String getValidateCode() {
		return validateCode;
	}
	/**
	 * @param validateCode the validateCode to set
	 */
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
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
	/**
	 * @return the newLoginName
	 */
	public String getNewLoginName() {
		return newLoginName;
	}
	/**
	 * @param newLoginName the newLoginName to set
	 */
	public void setNewLoginName(String newLoginName) {
		this.newLoginName = newLoginName;
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
	
}