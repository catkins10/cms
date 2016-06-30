package com.yuanluesoft.job.talent.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才:项目经验(job_talent_project)
 * @author linchuan
 *
 */
public class JobTalentProject extends Record {
	private long talentId; //人才ID
	private Date startDate; //开始时间
	private Date endDate; //结束时间
	private String projectName; //项目名称
	private String description; //项目描述
	private String duty; //责任描述
	private int showInResume = 1; //显示在简历中
	
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
	 * @return the duty
	 */
	public String getDuty() {
		return duty;
	}
	/**
	 * @param duty the duty to set
	 */
	public void setDuty(String duty) {
		this.duty = duty;
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
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the showInResume
	 */
	public int getShowInResume() {
		return showInResume;
	}
	/**
	 * @param showInResume the showInResume to set
	 */
	public void setShowInResume(int showInResume) {
		this.showInResume = showInResume;
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
}