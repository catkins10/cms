package com.yuanluesoft.j2oa.info.forms;

import java.sql.Date;

import com.yuanluesoft.jeaf.application.forms.ApplicationView;

/**
 * 
 * @author linchuan
 *
 */
public class IssueStatistic extends ApplicationView {
	private Date beginDate; //开始时间
	private Date endDate; //结束时间
	private boolean showCategory = true; //是否显示单位分类
	private boolean hideEmpty; //是否隐藏空记录
	private long parentOrgId; //组织机构ID
	private String parentOrgName; //组织机构名称
	
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
	/**
	 * @return the hideEmpty
	 */
	public boolean isHideEmpty() {
		return hideEmpty;
	}
	/**
	 * @param hideEmpty the hideEmpty to set
	 */
	public void setHideEmpty(boolean hideEmpty) {
		this.hideEmpty = hideEmpty;
	}
	/**
	 * @return the showCategory
	 */
	public boolean isShowCategory() {
		return showCategory;
	}
	/**
	 * @param showCategory the showCategory to set
	 */
	public void setShowCategory(boolean showCategory) {
		this.showCategory = showCategory;
	}
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
	 * @return the parentOrgName
	 */
	public String getParentOrgName() {
		return parentOrgName;
	}
	/**
	 * @param parentOrgName the parentOrgName to set
	 */
	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
}