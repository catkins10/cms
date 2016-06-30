package com.yuanluesoft.appraise.model;

import java.util.Set;

/**
 * 单位评议(按分类)
 * @author linchuan
 *
 */
public class UnitAppraisesByCategory {
	private String category; //分类名称
	private Set unitAppraises; //单位评议列表
	
	/**
	 * @return the unitAppraises
	 */
	public Set getUnitAppraises() {
		return unitAppraises;
	}
	/**
	 * @param unitAppraises the unitAppraises to set
	 */
	public void setUnitAppraises(Set unitAppraises) {
		this.unitAppraises = unitAppraises;
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
}