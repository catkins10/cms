package com.yuanluesoft.bidding.project.report.forms.admin;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 招标项目统计报表
 * @author linchuan
 *
 */
public class ProjectReport extends ActionForm {
	private Date beginDate;
	private Date endDate;
	
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