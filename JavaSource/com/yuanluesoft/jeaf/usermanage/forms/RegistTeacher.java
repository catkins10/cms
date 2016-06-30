package com.yuanluesoft.jeaf.usermanage.forms;

/**
 * 
 * @author linchuan
 *
 */
public class RegistTeacher extends RegistPerson {
	private long schoolId;
	private String chargeClassName;
	private long chargeClassId;
	
	/**
	 * @return the chargeClassId
	 */
	public long getChargeClassId() {
		return chargeClassId;
	}
	/**
	 * @param chargeClassId the chargeClassId to set
	 */
	public void setChargeClassId(long chargeClassId) {
		this.chargeClassId = chargeClassId;
	}
	/**
	 * @return the chargeClassName
	 */
	public String getChargeClassName() {
		return chargeClassName;
	}
	/**
	 * @param chargeClassName the chargeClassName to set
	 */
	public void setChargeClassName(String chargeClassName) {
		this.chargeClassName = chargeClassName;
	}
	/**
	 * @return the schoolId
	 */
	public long getSchoolId() {
		return schoolId;
	}
	/**
	 * @param schoolId the schoolId to set
	 */
	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}
}
