package com.yuanluesoft.dpc.keyproject.report.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class FiveYearPlanReport extends ActionForm {
	private int fiveYearPlanNumber; //第几个五年计划
	private int year; //年度

	/**
	 * @return the fiveYearPlanNumber
	 */
	public int getFiveYearPlanNumber() {
		return fiveYearPlanNumber;
	}

	/**
	 * @param fiveYearPlanNumber the fiveYearPlanNumber to set
	 */
	public void setFiveYearPlanNumber(int fiveYearPlanNumber) {
		this.fiveYearPlanNumber = fiveYearPlanNumber;
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