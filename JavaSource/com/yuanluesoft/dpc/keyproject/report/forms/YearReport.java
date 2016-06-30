package com.yuanluesoft.dpc.keyproject.report.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class YearReport extends ActionForm {
	private int year;
	private int month;
	private String[] projectLevels; //项目级别

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
	 * @return the projectLevels
	 */
	public String[] getProjectLevels() {
		return projectLevels;
	}

	/**
	 * @param projectLevels the projectLevels to set
	 */
	public void setProjectLevels(String[] projectLevels) {
		this.projectLevels = projectLevels;
	}
}