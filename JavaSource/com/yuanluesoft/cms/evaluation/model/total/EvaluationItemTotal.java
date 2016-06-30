package com.yuanluesoft.cms.evaluation.model.total;

import java.util.List;


/**
 * 测评选项汇总
 * @author linchuan
 *
 */
public class EvaluationItemTotal {
	private long itemId; //被测评人ID
	private List optionTotals; //选项汇总(EvaluationOptionTotal列表)
	private int markCount; //打分次数
	private double score; //得分
	
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
	 * @return the markCount
	 */
	public int getMarkCount() {
		return markCount;
	}
	/**
	 * @param markCount the markCount to set
	 */
	public void setMarkCount(int markCount) {
		this.markCount = markCount;
	}
}