package com.yuanluesoft.enterprise.evaluation.department.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Evaluation extends ActionForm {
	private int evaluationMonth; //月份
	private int evaluationYear; //年度
	private Timestamp created; //考核时间
	private String creator; //考核人
	private long creatorId; //考核人ID
	private Set results; //考核结果
	
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
	 * @return the evaluationMonth
	 */
	public int getEvaluationMonth() {
		return evaluationMonth;
	}
	/**
	 * @param evaluationMonth the evaluationMonth to set
	 */
	public void setEvaluationMonth(int evaluationMonth) {
		this.evaluationMonth = evaluationMonth;
	}
	/**
	 * @return the evaluationYear
	 */
	public int getEvaluationYear() {
		return evaluationYear;
	}
	/**
	 * @param evaluationYear the evaluationYear to set
	 */
	public void setEvaluationYear(int evaluationYear) {
		this.evaluationYear = evaluationYear;
	}
	/**
	 * @return the results
	 */
	public Set getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(Set results) {
		this.results = results;
	}
}