package com.yuanluesoft.appraise.forms.admin;

import java.sql.Date;

import com.yuanluesoft.jeaf.application.forms.ApplicationView;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiseUnitArticleTotalView extends ApplicationView {
	private Date beginDate; //开始时间
	private Date endDate; //结束时间
	private String category; //单位分类
	
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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