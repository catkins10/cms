package com.yuanluesoft.fet.project.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Fair extends ActionForm {
	private String name; //活动名称
	private String shortName; //简写
	private String fairNumber; //当前第几届
	private String category; //类别,比如三明赴港招商是一种专场招商，类别允许用户设置
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
	 * @return the fairNumber
	 */
	public String getFairNumber() {
		return fairNumber;
	}
	/**
	 * @param fairNumber the fairNumber to set
	 */
	public void setFairNumber(String fairNumber) {
		this.fairNumber = fairNumber;
	}
}