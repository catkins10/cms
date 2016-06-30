package com.yuanluesoft.cms.evaluation.model.total;

import java.util.List;

/**
 * 测评汇总
 * @author linchuan
 *
 */
public class EvaluationTotal {
	private long targetPersonId; //被测评人ID
	private String targetPersonName; //被测评人姓名
	private String targetPersonOrg; //被测评人所在部门
	private List itemTotals; //测评项目汇总(EvaluationItemTotal列表)
	private List optionTotals; //选项汇总(EvaluationOptionTotal列表)
	private double score; //得分
	
	/**
	 * @return the optionTotals
	 */
	public List getOptionTotals() {
		return optionTotals;
	}
	/**
	 * @param optionTotals the optionTotals to set
	 */
	public void setOptionTotals(List optionTotals) {
		this.optionTotals = optionTotals;
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
	 * @return the targetPersonId
	 */
	public long getTargetPersonId() {
		return targetPersonId;
	}
	/**
	 * @param targetPersonId the targetPersonId to set
	 */
	public void setTargetPersonId(long targetPersonId) {
		this.targetPersonId = targetPersonId;
	}
	/**
	 * @return the targetPersonName
	 */
	public String getTargetPersonName() {
		return targetPersonName;
	}
	/**
	 * @param targetPersonName the targetPersonName to set
	 */
	public void setTargetPersonName(String targetPersonName) {
		this.targetPersonName = targetPersonName;
	}
	/**
	 * @return the targetPersonOrg
	 */
	public String getTargetPersonOrg() {
		return targetPersonOrg;
	}
	/**
	 * @param targetPersonOrg the targetPersonOrg to set
	 */
	public void setTargetPersonOrg(String targetPersonOrg) {
		this.targetPersonOrg = targetPersonOrg;
	}
	/**
	 * @return the itemTotals
	 */
	public List getItemTotals() {
		return itemTotals;
	}
	/**
	 * @param itemTotals the itemTotals to set
	 */
	public void setItemTotals(List itemTotals) {
		this.itemTotals = itemTotals;
	}
}