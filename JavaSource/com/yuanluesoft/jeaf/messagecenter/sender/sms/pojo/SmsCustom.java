/*
 * Created on 2005-11-27
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender.sms.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 *
 * @author LinChuan
 * 
 *
 */
public class SmsCustom extends Record {
	private long personId;
	private java.lang.String mobile;
	
	/**
	 * @return Returns the personId.
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return Returns the mobile.
	 */
	public java.lang.String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile The mobile to set.
	 */
	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}
}
