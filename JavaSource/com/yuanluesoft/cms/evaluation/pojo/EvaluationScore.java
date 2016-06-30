package com.yuanluesoft.cms.evaluation.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 民主测评:评分(evaluation_score)
 * @author linchuan
 *
 */
public class EvaluationScore extends Record {
	private long markId; //测评记录ID
	private long itemId; //测评项目ID
	private long optionId; //选择项ID
	private String option; //选择项名称
	private double score; //分值
	private String remark; //备注
	
	/**
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the markId
	 */
	public long getMarkId() {
		return markId;
	}
	/**
	 * @param markId the markId to set
	 */
	public void setMarkId(long markId) {
		this.markId = markId;
	}
	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}
	/**
	 * @return the optionId
	 */
	public long getOptionId() {
		return optionId;
	}
	/**
	 * @param optionId the optionId to set
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
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