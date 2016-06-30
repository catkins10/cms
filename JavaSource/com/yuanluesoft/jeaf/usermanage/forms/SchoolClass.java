package com.yuanluesoft.jeaf.usermanage.forms;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class SchoolClass extends com.yuanluesoft.jeaf.usermanage.forms.admin.SchoolClass {
	private List students; //学生列表

	/**
	 * @return the students
	 */
	public List getStudents() {
		return students;
	}

	/**
	 * @param students the students to set
	 */
	public void setStudents(List students) {
		this.students = students;
	}
}