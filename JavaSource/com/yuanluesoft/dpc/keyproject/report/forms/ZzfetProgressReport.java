package com.yuanluesoft.dpc.keyproject.report.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ZzfetProgressReport extends ActionForm {
	private int year;
	private int month;
	private int keyMonth = 8; //大干150天的起始月份
	private String developmentArea;
	
	/**
	 * @return the developmentArea
	 */
	public String getDevelopmentArea() {
		return developmentArea;
	}
	/**
	 * @param developmentArea the developmentArea to set
	 */
	public void setDevelopmentArea(String developmentArea) {
		this.developmentArea = developmentArea;
	}
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
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
	/**
	 * @return the keyMonth
	 */
	public int getKeyMonth() {
		return keyMonth;
	}
	/**
	 * @param keyMonth the keyMonth to set
	 */
	public void setKeyMonth(int keyMonth) {
		this.keyMonth = keyMonth;
	}
}