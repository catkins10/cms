package com.yuanluesoft.appraise.forms.admin;

import com.yuanluesoft.jeaf.application.forms.ApplicationView;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiseResultView extends ApplicationView {
	private int year; //年度
	private String category; //单位分类
	
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
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
}