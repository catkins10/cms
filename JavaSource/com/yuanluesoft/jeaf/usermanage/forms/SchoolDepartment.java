package com.yuanluesoft.jeaf.usermanage.forms;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class SchoolDepartment extends com.yuanluesoft.jeaf.usermanage.forms.admin.SchoolDepartment {
	private long schoolId;
	private List teachers; //部门里的教师列表

	/**
	 * @return the teachers
	 */
	public List getTeachers() {
		return teachers;
	}

	/**
	 * @param teachers the teachers to set
	 */
	public void setTeachers(List teachers) {
		this.teachers = teachers;
	}

	/**
	 * @return the schoolId
	 */
	public long getSchoolId() {
		return schoolId;
	}

	/**
	 * @param schoolId the schoolId to set
	 */
	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

}