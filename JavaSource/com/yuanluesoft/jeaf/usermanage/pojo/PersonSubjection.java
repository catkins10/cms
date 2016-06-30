/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * @author Administrator
 *
 *
 */
public class PersonSubjection extends Record {
	private long personId;
	private long orgId;
	
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
	 * @return Returns the orgId.
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
}
