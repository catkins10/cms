package com.yuanluesoft.enterprise.workload.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 工作量考核:成绩(workload_assess_result)
 * @author linchuan
 *
 */
public class WorkloadAssessResult extends Record {
	private long assessId; //考核ID
	private long personId; //被考核人ID
	private String personName; //被考核人
	private double workload; //工作量
	private String remark; //考核说明
	private WorkloadAssess assess; //考核
	
	/**
	 * @return the assessId
	 */
	public long getAssessId() {
		return assessId;
	}
	/**
	 * @param assessId the assessId to set
	 */
	public void setAssessId(long assessId) {
		this.assessId = assessId;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
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
	 * @return the workload
	 */
	public double getWorkload() {
		return workload;
	}
	/**
	 * @param workload the workload to set
	 */
	public void setWorkload(double workload) {
		this.workload = workload;
	}
	/**
	 * @return the assess
	 */
	public WorkloadAssess getAssess() {
		return assess;
	}
	/**
	 * @param assess the assess to set
	 */
	public void setAssess(WorkloadAssess assess) {
		this.assess = assess;
	}
}