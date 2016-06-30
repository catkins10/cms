package com.yuanluesoft.cms.monitor.forms;

import java.sql.Date;

import com.yuanluesoft.jeaf.application.forms.ApplicationView;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorStat extends ApplicationView {
	private long parentOrgId; //父机构ID,用户有管理权限或者监察权限
	private Date beginDate; //起始日期
	private Date endDate; //结束日期

	/**
	 * @return the parentOrgId
	 */
	public long getParentOrgId() {
		return parentOrgId;
	}

	/**
	 * @param parentOrgId the parentOrgId to set
	 */
	public void setParentOrgId(long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}