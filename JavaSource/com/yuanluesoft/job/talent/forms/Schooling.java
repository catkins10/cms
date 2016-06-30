package com.yuanluesoft.job.talent.forms;

import java.sql.Date;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Schooling extends ActionForm {
	private long talentId; //人才ID
	private Date startDate; //开始时间
	private Date endDate; //结束时间
	private String school; //学校
	private String department; //院系
	private String specialty; //专业
	private long schoolClassId; //所在班级ID
	private String schoolClass; //所在班级
	private int qualification; //学历,初中,高中,职业高中,职业中专,中专,大专,本科,MBA,硕士,博士
	private String degree; //学位
	private String description; //专业描述
	private int abroad; //海外学习经历
	
	/**
	 * @return the abroad
	 */
	public int getAbroad() {
		return abroad;
	}
	/**
	 * @param abroad the abroad to set
	 */
	public void setAbroad(int abroad) {
		this.abroad = abroad;
	}
	/**
	 * @return the degree
	 */
	public String getDegree() {
		return degree;
	}
	/**
	 * @param degree the degree to set
	 */
	public void setDegree(String degree) {
		this.degree = degree;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the qualification
	 */
	public int getQualification() {
		return qualification;
	}
	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(int qualification) {
		this.qualification = qualification;
	}
	/**
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}
	/**
	 * @param school the school to set
	 */
	public void setSchool(String school) {
		this.school = school;
	}
	/**
	 * @return the specialty
	 */
	public String getSpecialty() {
		return specialty;
	}
	/**
	 * @param specialty the specialty to set
	 */
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the talentId
	 */
	public long getTalentId() {
		return talentId;
	}
	/**
	 * @param talentId the talentId to set
	 */
	public void setTalentId(long talentId) {
		this.talentId = talentId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSchoolClass() {
		return schoolClass;
	}
	public void setSchoolClass(String schoolClass) {
		this.schoolClass = schoolClass;
	}
	/**
	 * @return the schoolClassId
	 */
	public long getSchoolClassId() {
		return schoolClassId;
	}
	/**
	 * @param schoolClassId the schoolClassId to set
	 */
	public void setSchoolClassId(long schoolClassId) {
		this.schoolClassId = schoolClassId;
	}
}