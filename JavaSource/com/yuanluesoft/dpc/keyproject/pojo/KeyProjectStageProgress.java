package com.yuanluesoft.dpc.keyproject.pojo;

import java.sql.Date;

/**
 * 关键节点安排(keyproject_stage_progress)
 * @author linchuan
 *
 */
public class KeyProjectStageProgress extends KeyProjectComponent {
	private String stage; //节点名称
	private Date timeLimit; //完成时限
	private String responsibleUnit; //责任单位
	private String responsiblePerson; //责任人
	private String plan; //安排
	private char completed = '0'; //是否已提交完成情况
	private String progress; //完成进度
	/**
	 * @return the completed
	 */
	public char getCompleted() {
		return completed;
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(char completed) {
		this.completed = completed;
	}
	/**
	 * @return the plan
	 */
	public String getPlan() {
		return plan;
	}
	/**
	 * @param plan the plan to set
	 */
	public void setPlan(String plan) {
		this.plan = plan;
	}
	/**
	 * @return the progress
	 */
	public String getProgress() {
		return progress;
	}
	/**
	 * @param progress the progress to set
	 */
	public void setProgress(String progress) {
		this.progress = progress;
	}
	/**
	 * @return the responsiblePerson
	 */
	public String getResponsiblePerson() {
		return responsiblePerson;
	}
	/**
	 * @param responsiblePerson the responsiblePerson to set
	 */
	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}
	/**
	 * @return the responsibleUnit
	 */
	public String getResponsibleUnit() {
		return responsibleUnit;
	}
	/**
	 * @param responsibleUnit the responsibleUnit to set
	 */
	public void setResponsibleUnit(String responsibleUnit) {
		this.responsibleUnit = responsibleUnit;
	}
	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}
	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}
	/**
	 * @return the timeLimit
	 */
	public Date getTimeLimit() {
		return timeLimit;
	}
	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(Date timeLimit) {
		this.timeLimit = timeLimit;
	}
}