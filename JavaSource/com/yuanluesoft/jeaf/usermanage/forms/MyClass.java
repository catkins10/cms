package com.yuanluesoft.jeaf.usermanage.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class MyClass extends ActionForm {
	private String className; //班级名称
	private List classTeachers; //任课老师和班主任
	private List students; //学生列表
	private List toApprovalStudents; //待审批的学生列表
	
	private boolean pass; //是否审批通过
	
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the classTeachers
	 */
	public List getClassTeachers() {
		return classTeachers;
	}
	/**
	 * @param classTeachers the classTeachers to set
	 */
	public void setClassTeachers(List classTeachers) {
		this.classTeachers = classTeachers;
	}
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
	/**
	 * @return the toApprovalStudents
	 */
	public List getToApprovalStudents() {
		return toApprovalStudents;
	}
	/**
	 * @param toApprovalStudents the toApprovalStudents to set
	 */
	public void setToApprovalStudents(List toApprovalStudents) {
		this.toApprovalStudents = toApprovalStudents;
	}
	/**
	 * @return the pass
	 */
	public boolean isPass() {
		return pass;
	}
	/**
	 * @param pass the pass to set
	 */
	public void setPass(boolean pass) {
		this.pass = pass;
	}
}