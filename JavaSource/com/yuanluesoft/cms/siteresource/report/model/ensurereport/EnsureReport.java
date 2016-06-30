package com.yuanluesoft.cms.siteresource.report.model.ensurereport;

import java.util.List;

/**
 * 信息保障报表
 * @author linchuan
 *
 */
public class EnsureReport {
	private List months; //月份列表
	private List unitCategories; //单位分类列表
	
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
}