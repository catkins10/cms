package com.yuanluesoft.dpc.keyproject.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 项目:重点项目申报(keyproject_declare)
 * @author linchuan
 *
 */
public class KeyProjectDeclare extends Record {
	private long projectId; //项目ID
	private int declareYear; //申报年度
	private Timestamp declareTime; //申报时间
	private char isKeyProject = '0'; //是否列入重点项目
	private Timestamp approvalTime; //批准日期
	private float priority; //优先级
	
	private KeyProject project; //项目
	
	/**
	 * @return the approvalTime
	 */
	public Timestamp getApprovalTime() {
		return approvalTime;
	}
	/**
	 * @param approvalTime the approvalTime to set
	 */
	public void setApprovalTime(Timestamp approvalTime) {
		this.approvalTime = approvalTime;
	}
	/**
	 * @return the declareTime
	 */
	public Timestamp getDeclareTime() {
		return declareTime;
	}
	/**
	 * @param declareTime the declareTime to set
	 */
	public void setDeclareTime(Timestamp declareTime) {
		this.declareTime = declareTime;
	}
	/**
	 * @return the declareYear
	 */
	public int getDeclareYear() {
		return declareYear;
	}
	/**
	 * @param declareYear the declareYear to set
	 */
	public void setDeclareYear(int declareYear) {
		this.declareYear = declareYear;
	}
	/**
	 * @return the isKeyProject
	 */
	public char getIsKeyProject() {
		return isKeyProject;
	}
	/**
	 * @param isKeyProject the isKeyProject to set
	 */
	public void setIsKeyProject(char isKeyProject) {
		this.isKeyProject = isKeyProject;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the project
	 */
	public KeyProject getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(KeyProject project) {
		this.project = project;
	}
}