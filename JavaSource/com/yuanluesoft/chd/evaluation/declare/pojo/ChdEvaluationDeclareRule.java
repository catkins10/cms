package com.yuanluesoft.chd.evaluation.declare.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评价:评价细则完成情况(chd_eval_declar_rule)
 * @author linchuan
 *
 */
public class ChdEvaluationDeclareRule extends Record {
	private long declareId; //申报ID
	private long ruleId; //细则ID
	private String ruleNumber; //编号
	private String rule; //细则
	private String result; //自评结果
	private double score; //自评分
	private String approvedResult; //考核结果
	private double approvedScore; //考核分数
	private String remark; //备注
	
	/**
	 * @return the approvedResult
	 */
	public String getApprovedResult() {
		return approvedResult;
	}
	/**
	 * @param approvedResult the approvedResult to set
	 */
	public void setApprovedResult(String approvedResult) {
		this.approvedResult = approvedResult;
	}
	/**
	 * @return the approvedScore
	 */
	public double getApprovedScore() {
		return approvedScore;
	}
	/**
	 * @param approvedScore the approvedScore to set
	 */
	public void setApprovedScore(double approvedScore) {
		this.approvedScore = approvedScore;
	}
	/**
	 * @return the declareId
	 */
	public long getDeclareId() {
		return declareId;
	}
	/**
	 * @param declareId the declareId to set
	 */
	public void setDeclareId(long declareId) {
		this.declareId = declareId;
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
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the rule
	 */
	public String getRule() {
		return rule;
	}
	/**
	 * @param rule the rule to set
	 */
	public void setRule(String rule) {
		this.rule = rule;
	}
	/**
	 * @return the ruleId
	 */
	public long getRuleId() {
		return ruleId;
	}
	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}
	/**
	 * @return the ruleNumber
	 */
	public String getRuleNumber() {
		return ruleNumber;
	}
	/**
	 * @param ruleNumber the ruleNumber to set
	 */
	public void setRuleNumber(String ruleNumber) {
		this.ruleNumber = ruleNumber;
	}
	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

}