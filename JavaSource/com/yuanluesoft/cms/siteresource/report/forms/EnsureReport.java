package com.yuanluesoft.cms.siteresource.report.forms;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 信息保障报表
 * @author linchuan
 *
 */
public class EnsureReport extends ActionForm {
	private long siteId; //站点ID
	private String siteName; //站点名称
	private Date beginDate; //开始时间
	private Date endDate; //结束时间
	private int quarter; //季度
	private List months; //月份列表
	private List unitCategories; //单位分类列表
	
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
	/**
	 * @return the unitCategories
	 */
	public List getUnitCategories() {
		return unitCategories;
	}
	/**
	 * @param unitCategories the unitCategories to set
	 */
	public void setUnitCategories(List unitCategories) {
		this.unitCategories = unitCategories;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
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
	 * @return the months
	 */
	public List getMonths() {
		return months;
	}
	/**
	 * @param months the months to set
	 */
	public void setMonths(List months) {
		this.months = months;
	}
	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}
	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * @return the quarter
	 */
	public int getQuarter() {
		return quarter;
	}
	/**
	 * @param quarter the quarter to set
	 */
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
}