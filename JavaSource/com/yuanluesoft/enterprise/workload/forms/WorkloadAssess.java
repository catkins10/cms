package com.yuanluesoft.enterprise.workload.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class WorkloadAssess extends ActionForm {
	private int assessYear; //年度
	private int assessMonth; //月份
	private long creatorId; //考核人ID
	private String creator; //考核人
	private Timestamp created; //考核时间
	private Set results; //成绩列表
	
	/**
	 * @return the assessMonth
	 */
	public int getAssessMonth() {
		return assessMonth;
	}
	/**
	 * @param assessMonth the assessMonth to set
	 */
	public void setAssessMonth(int assessMonth) {
		this.assessMonth = assessMonth;
	}
	/**
	 * @return the assessYear
	 */
	public int getAssessYear() {
		return assessYear;
	}
	/**
	 * @param assessYear the assessYear to set
	 */
	public void setAssessYear(int assessYear) {
		this.assessYear = assessYear;
	}
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