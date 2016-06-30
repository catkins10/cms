package com.yuanluesoft.emp.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

public class Department extends ActionForm {
	private String departName;	//部门名称
	private String description;
	private Set emps;

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getEmps() {
		return emps;
	}

	public void setEmps(Set emps) {
		this.emps = emps;
	}
}
