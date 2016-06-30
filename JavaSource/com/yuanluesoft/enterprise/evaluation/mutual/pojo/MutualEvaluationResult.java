package com.yuanluesoft.enterprise.evaluation.mutual.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 互评:结果(evaluation_mutual_result)
 * @author linchuan
 *
 */
public class MutualEvaluationResult extends Record {
	private long evaluationId; //互评ID
	private long personId; //被评价人ID
	private String personName; //被评价人
	private int evaluateLevel; //评价等级,0/靠后,1/居中,2/靠前
	private MutualEvaluation evaluation; //互评
	
	//扩展属性,统计时有效
	private int highEvaluateLevelCount; //靠前数量
	private int lowEvaluateLevelCount; //靠后数量
	
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
	 * @return the evaluateLevel
	 */
	public int getEvaluateLevel() {
		return evaluateLevel;
	}
	/**
	 * @param evaluateLevel the evaluateLevel to set
	 */
	public void setEvaluateLevel(int evaluateLevel) {
		this.evaluateLevel = evaluateLevel;
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
	 * @return the evaluation
	 */
	public MutualEvaluation getEvaluation() {
		return evaluation;
	}
	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(MutualEvaluation evaluation) {
		this.evaluation = evaluation;
	}
	/**
	 * @return the highEvaluateLevelCount
	 */
	public int getHighEvaluateLevelCount() {
		return highEvaluateLevelCount;
	}
	/**
	 * @param highEvaluateLevelCount the highEvaluateLevelCount to set
	 */
	public void setHighEvaluateLevelCount(int highEvaluateLevelCount) {
		this.highEvaluateLevelCount = highEvaluateLevelCount;
	}
	/**
	 * @return the lowEvaluateLevelCount
	 */
	public int getLowEvaluateLevelCount() {
		return lowEvaluateLevelCount;
	}
	/**
	 * @param lowEvaluateLevelCount the lowEvaluateLevelCount to set
	 */
	public void setLowEvaluateLevelCount(int lowEvaluateLevelCount) {
		this.lowEvaluateLevelCount = lowEvaluateLevelCount;
	}
}