package com.yuanluesoft.jeaf.usermanage.security.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author chuan
 *
 */
public class UserPasswordPolicy extends ActionForm {
	private long orgId; //单位ID
	private String orgName; //单位名称
	private int internalPasswordStrength; //内部用户密码强度,低/1,中/2,高/3
	private int externalPasswordStrength; //外部用户密码强度,低/1,中/2,高/3
	private int internalPasswordDays; //内部用户密码修改周期
	private int externalPasswordDays; //外部用户密码修改周期
	private int passwordWrong; //密码输错次数控制,针对内部用户,超出后自动停用
	private String resetPasswordMailSubject; //密码重置邮件标题
	private String resetPasswordMailContent; //密码重置邮件内容
	private int resetPasswordCodeExpire; //密码重置验证码有效期,以分钟为单位
	
	/**
	 * @return the externalPasswordDays
	 */
	public int getExternalPasswordDays() {
		return externalPasswordDays;
	}
	/**
	 * @param externalPasswordDays the externalPasswordDays to set
	 */
	public void setExternalPasswordDays(int externalPasswordDays) {
		this.externalPasswordDays = externalPasswordDays;
	}
	/**
	 * @return the externalPasswordStrength
	 */
	public int getExternalPasswordStrength() {
		return externalPasswordStrength;
	}
	/**
	 * @param externalPasswordStrength the externalPasswordStrength to set
	 */
	public void setExternalPasswordStrength(int externalPasswordStrength) {
		this.externalPasswordStrength = externalPasswordStrength;
	}
	/**
	 * @return the internalPasswordDays
	 */
	public int getInternalPasswordDays() {
		return internalPasswordDays;
	}
	/**
	 * @param internalPasswordDays the internalPasswordDays to set
	 */
	public void setInternalPasswordDays(int internalPasswordDays) {
		this.internalPasswordDays = internalPasswordDays;
	}
	/**
	 * @return the internalPasswordStrength
	 */
	public int getInternalPasswordStrength() {
		return internalPasswordStrength;
	}
	/**
	 * @param internalPasswordStrength the internalPasswordStrength to set
	 */
	public void setInternalPasswordStrength(int internalPasswordStrength) {
		this.internalPasswordStrength = internalPasswordStrength;
	}
	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the passwordWrong
	 */
	public int getPasswordWrong() {
		return passwordWrong;
	}
	/**
	 * @param passwordWrong the passwordWrong to set
	 */
	public void setPasswordWrong(int passwordWrong) {
		this.passwordWrong = passwordWrong;
	}
	/**
	 * @return the resetPasswordCodeExpire
	 */
	public int getResetPasswordCodeExpire() {
		return resetPasswordCodeExpire;
	}
	/**
	 * @param resetPasswordCodeExpire the resetPasswordCodeExpire to set
	 */
	public void setResetPasswordCodeExpire(int resetPasswordCodeExpire) {
		this.resetPasswordCodeExpire = resetPasswordCodeExpire;
	}
	/**
	 * @return the resetPasswordMailContent
	 */
	public String getResetPasswordMailContent() {
		return resetPasswordMailContent;
	}
	/**
	 * @param resetPasswordMailContent the resetPasswordMailContent to set
	 */
	public void setResetPasswordMailContent(String resetPasswordMailContent) {
		this.resetPasswordMailContent = resetPasswordMailContent;
	}
	/**
	 * @return the resetPasswordMailSubject
	 */
	public String getResetPasswordMailSubject() {
		return resetPasswordMailSubject;
	}
	/**
	 * @param resetPasswordMailSubject the resetPasswordMailSubject to set
	 */
	public void setResetPasswordMailSubject(String resetPasswordMailSubject) {
		this.resetPasswordMailSubject = resetPasswordMailSubject;
	}
}