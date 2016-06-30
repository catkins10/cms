package com.yuanluesoft.municipal.facilities.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * PDA使用者
 * @author linchuan
 *
 */
public class PdaUser extends ActionForm {
	private String name; //使用者姓名
	private String code; //工号
	private String pdaNumber; //PDA号码
	private long orgId; //所在组织机构ID
	private String orgName; //所在组织机构名称
	
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
	 * @return the pdaNumber
	 */
	public String getPdaNumber() {
		return pdaNumber;
	}
	/**
	 * @param pdaNumber the pdaNumber to set
	 */
	public void setPdaNumber(String pdaNumber) {
		this.pdaNumber = pdaNumber;
	}
}
