package com.yuanluesoft.emp.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

public class Department extends Record {
	private String departName;	//部门名称
	private String description;	//描述
	private Set emps;	//该部门下辖员工

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
