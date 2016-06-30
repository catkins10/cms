package com.yuanluesoft.job.company.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class RefreshAllJobs extends ActionForm {
	private int refreshTimesLeft; //剩余刷新次数
	private Timestamp lastRefreshTime; //最后刷新时间
	
	/**
	 * @return the lastRefreshTime
	 */
	public Timestamp getLastRefreshTime() {
		return lastRefreshTime;
	}
	/**
	 * @param lastRefreshTime the lastRefreshTime to set
	 */
	public void setLastRefreshTime(Timestamp lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}
	/**
	 * @return the refreshTimesLeft
	 */
	public int getRefreshTimesLeft() {
		return refreshTimesLeft;
	}
	/**
	 * @param refreshTimesLeft the refreshTimesLeft to set
	 */
	public void setRefreshTimesLeft(int refreshTimesLeft) {
		this.refreshTimesLeft = refreshTimesLeft;
	}
}