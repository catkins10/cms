package com.yuanluesoft.chd.evaluation.selfeval.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 自评(chd_eval_self)
 * @author linchuan
 *
 */
public class ChdEvaluationSelf extends WorkflowData {
	private int evalYear; //年度
	private int evalMonth; //月份O
	private String evalResult; //自查情况说明
	private Timestamp created; //提交时间
	private String department; //部门名称
	private long creatorId; //责任人ID
	private String creator; //责任人姓名
	private String remark; //备注
	private String workflowInstanceId; //工作流实例ID
	private Set subjections; //隶属目录
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * @return the evalMonth
	 */
	public int getEvalMonth() {
		return evalMonth;
	}
	/**
	 * @param evalMonth the evalMonth to set
	 */
	public void setEvalMonth(int evalMonth) {
		this.evalMonth = evalMonth;
	}
	/**
	 * @return the evalResult
	 */
	public String getEvalResult() {
		return evalResult;
	}
	/**
	 * @param evalResult the evalResult to set
	 */
	public void setEvalResult(String evalResult) {
		this.evalResult = evalResult;
	}
	/**
	 * @return the evalYear
	 */
	public int getEvalYear() {
		return evalYear;
	}
	/**
	 * @param evalYear the evalYear to set
	 */
	public void setEvalYear(int evalYear) {
		this.evalYear = evalYear;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the workflowInstanceId
	 */
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}
	/**
	 * @param workflowInstanceId the workflowInstanceId to set
	 */
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}
	/**
	 * @return the subjections
	 */
	public Set getSubjections() {
		return subjections;
	}
	/**
	 * @param subjections the subjections to set
	 */
	public void setSubjections(Set subjections) {
		this.subjections = subjections;
	}
}