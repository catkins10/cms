package com.yuanluesoft.enterprise.evaluation.department.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 部门考核:参数配置(evaluation_department_param)
 * @author linchuan
 *
 */
public class DepartmentEvaluationParameter extends Record {
	private String dropoutDepartmentIds; //不参与的部门ID
	private String dropoutDepartments; //不参与的部门
	
	/**
	 * @return the dropoutDepartmentIds
	 */
	public String getDropoutDepartmentIds() {
		return dropoutDepartmentIds;
	}
	/**
	 * @param dropoutDepartmentIds the dropoutDepartmentIds to set
	 */
	public void setDropoutDepartmentIds(String dropoutDepartmentIds) {
		this.dropoutDepartmentIds = dropoutDepartmentIds;
	}
	/**
	 * @return the dropoutDepartments
	 */
	public String getDropoutDepartments() {
		return dropoutDepartments;
	}
	/**
	 * @param dropoutDepartments the dropoutDepartments to set
	 */
	public void setDropoutDepartments(String dropoutDepartments) {
		this.dropoutDepartments = dropoutDepartments;
	}
}