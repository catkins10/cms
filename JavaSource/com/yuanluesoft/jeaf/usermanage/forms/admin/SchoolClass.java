/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.forms.admin;

import java.util.List;
import java.util.Set;


/**
 * @author Administrator
 *
 *
 */
public class SchoolClass extends Org {
	private int enrollTime;
	private int classNumber;
	private int lengthOfSchooling;
	private char isGraduated = '0'; //是否毕业
	
	//教师相关
	private String teacherTitle;
	private long teacherId;
	private String teacherName;
	
	private Set teachers; //任课老师列表
	
	private String grade; //年级
	private List grades; //年级列表,供选择
	private List teacherTitles; //教师称呼列表
	
	private String webApplicationSafeUrl;
	
	/**
	 * @return the teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}
	/**
	 * @param teacherName the teacherName to set
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	/**
	 * @return Returns the classNumber.
	 */
	public int getClassNumber() {
		return classNumber;
	}
	/**
	 * @param classNumber The classNumber to set.
	 */
	public void setClassNumber(int classNumber) {
		this.classNumber = classNumber;
	}
	/**
	 * @return Returns the enrollTime.
	 */
	public int getEnrollTime() {
		return enrollTime;
	}
	/**
	 * @param enrollTime The enrollTime to set.
	 */
	public void setEnrollTime(int enrollTime) {
		this.enrollTime = enrollTime;
	}
	/**
	 * @return the lengthOfSchooling
	 */
	public int getLengthOfSchooling() {
		return lengthOfSchooling;
	}
	/**
	 * @param lengthOfSchooling the lengthOfSchooling to set
	 */
	public void setLengthOfSchooling(int lengthOfSchooling) {
		this.lengthOfSchooling = lengthOfSchooling;
	}
	/**
	 * @return the teacherId
	 */
	public long getTeacherId() {
		return teacherId;
	}
	/**
	 * @param teacherId the teacherId to set
	 */
	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * @return the teacherTitle
	 */
	public String getTeacherTitle() {
		return teacherTitle;
	}
	/**
	 * @param teacherTitle the teacherTitle to set
	 */
	public void setTeacherTitle(String teacherTitle) {
		this.teacherTitle = teacherTitle;
	}
	/**
	 * @return the teachers
	 */
	public Set getTeachers() {
		return teachers;
	}
	/**
	 * @param teachers the teachers to set
	 */
	public void setTeachers(Set teachers) {
		this.teachers = teachers;
	}
	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return the grades
	 */
	public List getGrades() {
		return grades;
	}
	/**
	 * @param grades the grades to set
	 */
	public void setGrades(List grades) {
		this.grades = grades;
	}
	/**
	 * @return the teacherTitles
	 */
	public List getTeacherTitles() {
		return teacherTitles;
	}
	/**
	 * @param teacherTitles the teacherTitles to set
	 */
	public void setTeacherTitles(List teacherTitles) {
		this.teacherTitles = teacherTitles;
	}
	/**
	 * @return the webApplicationSafeUrl
	 */
	public String getWebApplicationSafeUrl() {
		return webApplicationSafeUrl;
	}
	/**
	 * @param webApplicationSafeUrl the webApplicationSafeUrl to set
	 */
	public void setWebApplicationSafeUrl(String webApplicationSafeUrl) {
		this.webApplicationSafeUrl = webApplicationSafeUrl;
	}
	/**
	 * @return the isGraduated
	 */
	public char getIsGraduated() {
		return isGraduated;
	}
	/**
	 * @param isGraduated the isGraduated to set
	 */
	public void setIsGraduated(char isGraduated) {
		this.isGraduated = isGraduated;
	}
}