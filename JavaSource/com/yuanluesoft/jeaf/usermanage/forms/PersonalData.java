package com.yuanluesoft.jeaf.usermanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class PersonalData extends ActionForm {
	private String userName;
	private String loginName;
	private String mailAddress;
	private String familyAddress;
	private String mobile;
	private String tel;
	private String telFamily;
	private long orgId; //用户所在组织ID
	private String orgFullName; //用户所在组织全称
	private int seatNumber; //座号
	private boolean isFinishSchool; //是否毕业
	private int type;
	
	/**
	 * @return the familyAddress
	 */
	public String getFamilyAddress() {
		return familyAddress;
	}
	/**
	 * @param familyAddress the familyAddress to set
	 */
	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}
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
	 * @return the orgFullName
	 */
	public String getOrgFullName() {
		return orgFullName;
	}
	/**
	 * @param orgFullName the orgFullName to set
	 */
	public void setOrgFullName(String orgFullName) {
		this.orgFullName = orgFullName;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the telFamily
	 */
	public String getTelFamily() {
		return telFamily;
	}
	/**
	 * @param telFamily the telFamily to set
	 */
	public void setTelFamily(String telFamily) {
		this.telFamily = telFamily;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
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
	 * @return the seatNumber
	 */
	public int getSeatNumber() {
		return seatNumber;
	}
	/**
	 * @param seatNumber the seatNumber to set
	 */
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	/**
	 * @return the isFinishSchool
	 */
	public boolean isFinishSchool() {
		return isFinishSchool;
	}
	/**
	 * @param isFinishSchool the isFinishSchool to set
	 */
	public void setFinishSchool(boolean isFinishSchool) {
		this.isFinishSchool = isFinishSchool;
	}

}