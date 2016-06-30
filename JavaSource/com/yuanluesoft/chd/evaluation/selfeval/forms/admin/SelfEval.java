package com.yuanluesoft.chd.evaluation.selfeval.forms.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class SelfEval extends WorkflowForm {
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

	//扩展属性
	private String directoryName; //隶属评价目录名称
	private long directoryId; //隶属评价目录ID
	private List dataList; //本月提交的资料
	
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
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	/**
	 * @return the dataList
	 */
	public List getDataList() {
		return dataList;
	}
	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
}