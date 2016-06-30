/*
 * Created on 2005-11-27
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender.msn.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class MsnCustom extends Record {
	private long personId;
	private String msnLoginName;
	private char isFriendly = '0';
	
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
	 * @return Returns the msnLoginName.
	 */
	public java.lang.String getMsnLoginName() {
		return msnLoginName;
	}
	
	/**
	 * @param msnLoginName The msnLoginName to set.
	 */
	public void setMsnLoginName(java.lang.String msnLoginName) {
		this.msnLoginName = msnLoginName;
	}

	/**
	 * @return the isFriendly
	 */
	public char getIsFriendly() {
		return isFriendly;
	}

	/**
	 * @param isFriendly the isFriendly to set
	 */
	public void setIsFriendly(char isFriendly) {
		this.isFriendly = isFriendly;
	}
}
