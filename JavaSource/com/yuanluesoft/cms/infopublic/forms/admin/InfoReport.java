package com.yuanluesoft.cms.infopublic.forms.admin;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 信息公开统计(光泽)
 * @author linchuan
 *
 */
public class InfoReport extends ActionForm {
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