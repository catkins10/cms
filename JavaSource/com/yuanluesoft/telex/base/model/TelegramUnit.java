package com.yuanluesoft.telex.base.model;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class TelegramUnit {
	private String category; //分类
	private List units; //单位列表
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
	 * @return the units
	 */
	public List getUnits() {
		return units;
	}
	/**
	 * @param units the units to set
	 */
	public void setUnits(List units) {
		this.units = units;
	}
}
