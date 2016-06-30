package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.usermanage.pojo.Org;

/**
 * 
 * @author linchuan
 *
 */
public class RegistTeacher extends RegistPerson {
	private String chargeClassName;
	private long chargeClassId;
	private Org school;
	
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
	 * @return the school
	 */
	public Org getSchool() {
		return school;
	}
	/**
	 * @param school the school to set
	 */
	public void setSchool(Org school) {
		this.school = school;
	}
}
