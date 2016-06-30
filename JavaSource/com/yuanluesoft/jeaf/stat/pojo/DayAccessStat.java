/**
 * 
 */
package com.yuanluesoft.jeaf.stat.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 *
 * @author LinChuan
 *
 */
public class DayAccessStat extends Record {
	private String applicationName;
	private long recordId;
	private String pageName;
	private Date accessDate;
	private long times;
	
	/**
	 * @return the accessDate
	 */
	public Date getAccessDate() {
		return accessDate;
	}
	/**
	 * @param accessDate the accessDate to set
	 */
	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	/**
	 * @return the recordId
	 */
	public long getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	/**
	 * @return the times
	 */
	public long getTimes() {
		return times;
	}
	/**
	 * @param times the times to set
	 */
	public void setTimes(long times) {
		this.times = times;
	}
}
