package com.yuanluesoft.chd.evaluation.selfeval.model;

import java.util.List;

/**
 * 自查按月分类
 * @author linchuan
 *
 */
public class SelfEvalByMonth {
	private int month; //月份
	private List departments; //部门列表
	
	/**
	 * @return the departments
	 */
	public List getDepartments() {
		return departments;
	}
	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(List departments) {
		this.departments = departments;
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
}