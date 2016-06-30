package com.yuanluesoft.enterprise.evaluation.department.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 部门考核:结果(evaluation_department_result)
 * @author linchuan
 *
 */
public class DepartmentEvaluationResult extends Record {
	private long evaluationId; //考核ID
	private long departmentId; //部门ID
	private String departmentName; //部门名称
	private double result; //权重
	private String remark; //考核说明
	private DepartmentEvaluation evaluation; //部门考核
	
	/**
	 * @return the departmentId
	 */
	public long getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	/**
	 * @return the evaluationId
	 */
	public long getEvaluationId() {
		return evaluationId;
	}
	/**
	 * @param evaluationId the evaluationId to set
	 */
	public void setEvaluationId(long evaluationId) {
		this.evaluationId = evaluationId;
	}
	/**
	 * @return the result
	 */
	public double getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(double result) {
		this.result = result;
	}
	/**
	 * @return the evaluation
	 */
	public DepartmentEvaluation getEvaluation() {
		return evaluation;
	}
	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(DepartmentEvaluation evaluation) {
		this.evaluation = evaluation;
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
}