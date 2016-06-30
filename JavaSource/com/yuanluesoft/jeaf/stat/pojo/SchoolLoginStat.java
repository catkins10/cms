/**
 * 
 */
package com.yuanluesoft.jeaf.stat.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 *
 * @author LinChuan
 *
 */
public class SchoolLoginStat extends Record {
	private long schoolId;
	private String schoolName;
	private Date loginDay;
	private int studentTimes;
	private int teacherNumber;
	private int studentNumber;
	private int teacherTimes;
	
	/**
	 * @return the loginDay
	 */
	public Date getLoginDay() {
		return loginDay;
	}
	/**
	 * @param loginDay the loginDay to set
	 */
	public void setLoginDay(Date loginDay) {
		this.loginDay = loginDay;
	}
	/**
	 * @return the studentNumber
	 */
	public int getStudentNumber() {
		return studentNumber;
	}
	/**
	 * @param studentNumber the studentNumber to set
	 */
	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}
	/**
	 * @return the studentTimes
	 */
	public int getStudentTimes() {
		return studentTimes;
	}
	/**
	 * @param studentTimes the studentTimes to set
	 */
	public void setStudentTimes(int studentTimes) {
		this.studentTimes = studentTimes;
	}
	/**
	 * @return the teacherNumber
	 */
	public int getTeacherNumber() {
		return teacherNumber;
	}
	/**
	 * @param teacherNumber the teacherNumber to set
	 */
	public void setTeacherNumber(int teacherNumber) {
		this.teacherNumber = teacherNumber;
	}
	/**
	 * @return the teacherTimes
	 */
	public int getTeacherTimes() {
		return teacherTimes;
	}
	/**
	 * @param teacherTimes the teacherTimes to set
	 */
	public void setTeacherTimes(int teacherTimes) {
		this.teacherTimes = teacherTimes;
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
	/**
	 * @return the schoolName
	 */
	public String getSchoolName() {
		return schoolName;
	}
	/**
	 * @param schoolName the schoolName to set
	 */
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
}
