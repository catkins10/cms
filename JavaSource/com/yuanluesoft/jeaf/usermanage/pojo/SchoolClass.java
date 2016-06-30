/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

import java.util.Set;

/**
 * 组织机构:班级(user_class)
 * @author linchuan
 *
 */
public class SchoolClass extends Org {
	private int enrollTime; //入学年份
	private int lengthOfSchooling; //学制,小学6年,初中3年,高中3年
	private int classNumber; //班级编号,自动生成班级名称
	private char isGraduated = '0'; //是否毕业
	private Set teachers; //任课老师列表

	/**
	 * 获取学习阶段
	 * @return
	 */
	public String getStage() {
		if(getDirectoryName().startsWith("高")) {
			return "高中";
		}
		else if(getDirectoryName().startsWith("初") ||
				getDirectoryName().startsWith("七") ||
				getDirectoryName().startsWith("八") ||
				getDirectoryName().startsWith("九")) {
			return "初中";
		}
		return "小学";
	}
	
	/**
	 * 获取年级
	 * @return
	 */
	public String getGrade() {
		if(getDirectoryName().startsWith("高") || getDirectoryName().startsWith("初")) {
			return getDirectoryName().substring(0, 2);
		}
		else {
			return getDirectoryName().substring(0, 2) + "级";
		}
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
