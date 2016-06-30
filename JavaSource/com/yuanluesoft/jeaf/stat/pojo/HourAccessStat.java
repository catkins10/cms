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
public class HourAccessStat extends Record {
	private Date accessDate;
	private int week;
	private int accessHour;
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
	/**
	 * @return the week
	 */
	public int getWeek() {
		return week;
	}
	/**
	 * @param week the week to set
	 */
	public void setWeek(int week) {
		this.week = week;
	}
	/**
	 * @return the accessHour
	 */
	public int getAccessHour() {
		return accessHour;
	}
	/**
	 * @param accessHour the accessHour to set
	 */
	public void setAccessHour(int accessHour) {
		this.accessHour = accessHour;
	}
}
