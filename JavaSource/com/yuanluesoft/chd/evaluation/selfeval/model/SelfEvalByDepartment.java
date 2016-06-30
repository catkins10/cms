package com.yuanluesoft.chd.evaluation.selfeval.model;

import java.util.List;

/**
 * 自查按部门分类
 * @author linchuan
 *
 */
public class SelfEvalByDepartment {
	private String department; //部门
	private List selfEvalList; //自查列表
	
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * @return the selfEvalList
	 */
	public List getSelfEvalList() {
		return selfEvalList;
	}
	/**
	 * @param selfEvalList the selfEvalList to set
	 */
	public void setSelfEvalList(List selfEvalList) {
		this.selfEvalList = selfEvalList;
	}
}