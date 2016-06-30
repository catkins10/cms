package com.yuanluesoft.enterprise.workreport.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class WorkReport extends WorkflowForm {
	private long reporterId; //汇报人ID
	private String reporterName; //汇报人姓名
	private long teamId; //项目组ID,不在项目组时为0
	private String workDescription; //前一阶段完成情况
	private String problem; //存在的问题
	private String plan; //下一阶段计划
	private String reportType; //汇报类型,日报/周报/月报/半年总结/年终总结/项目总结
	private Timestamp reportTime; //汇报时间
	
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
	 * @return the problem
	 */
	public String getProblem() {
		return problem;
	}
	/**
	 * @param problem the problem to set
	 */
	public void setProblem(String problem) {
		this.problem = problem;
	}
	/**
	 * @return the reporterId
	 */
	public long getReporterId() {
		return reporterId;
	}
	/**
	 * @param reporterId the reporterId to set
	 */
	public void setReporterId(long reporterId) {
		this.reporterId = reporterId;
	}
	/**
	 * @return the reporterName
	 */
	public String getReporterName() {
		return reporterName;
	}
	/**
	 * @param reporterName the reporterName to set
	 */
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}
	/**
	 * @return the reportTime
	 */
	public Timestamp getReportTime() {
		return reportTime;
	}
	/**
	 * @param reportTime the reportTime to set
	 */
	public void setReportTime(Timestamp reportTime) {
		this.reportTime = reportTime;
	}
	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	/**
	 * @return the teamId
	 */
	public long getTeamId() {
		return teamId;
	}
	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	/**
	 * @return the workDescription
	 */
	public String getWorkDescription() {
		return workDescription;
	}
	/**
	 * @param workDescription the workDescription to set
	 */
	public void setWorkDescription(String workDescription) {
		this.workDescription = workDescription;
	}
}