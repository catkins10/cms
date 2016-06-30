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
public class PersonJob extends Record {
	private long personId;
	private long jobId;
	
	/**
	 * @return Returns the jobId.
	 */
	public long getJobId() {
		return jobId;
	}
	/**
	 * @param jobId The jobId to set.
	 */
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
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
}
