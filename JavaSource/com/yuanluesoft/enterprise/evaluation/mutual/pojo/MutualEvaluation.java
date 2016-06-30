package com.yuanluesoft.enterprise.evaluation.mutual.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 互评(evaluation_mutual)
 * @author linchuan
 *
 */
public class MutualEvaluation extends Record {
	private int evaluationYear; //年度
	private int evaluationMonth; //月份
	private long orgId; //部门ID
	private String orgName; //部门名称
	private int voteNumber; //投票数
	private long creatorId; //评价人ID
	private String creator; //评价人
	private Timestamp created; //评价时间
	private Set results; //互评结果
	
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
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	/**
	 * @return the voteNumber
	 */
	public int getVoteNumber() {
		return voteNumber;
	}
	/**
	 * @param voteNumber the voteNumber to set
	 */
	public void setVoteNumber(int voteNumber) {
		this.voteNumber = voteNumber;
	}
}