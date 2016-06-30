package com.yuanluesoft.chd.evaluation.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 评价细则:评价等级对应的分数(chd_evaluation_rule_score)
 * @author linchuan
 *
 */
public class ChdEvaluationRuleScore extends Record {
	private long ruleId; //评价细则ID
	private long levelId; //评价等级ID
	private String level; //评价等级,一星级、二星级、三星级、四星级、五星级、省一星级...
	private int minScore; //最低分数
	private int maxScore; //最高分数
	
	/**
	 * 是否有效
	 * @return
	 */
	public boolean isValid() {
		return minScore>0 || maxScore>0;
	}
	
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * @return the levelId
	 */
	public long getLevelId() {
		return levelId;
	}
	/**
	 * @param levelId the levelId to set
	 */
	public void setLevelId(long levelId) {
		this.levelId = levelId;
	}
	/**
	 * @return the maxScore
	 */
	public int getMaxScore() {
		return maxScore;
	}
	/**
	 * @param maxScore the maxScore to set
	 */
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
	/**
	 * @return the minScore
	 */
	public int getMinScore() {
		return minScore;
	}
	/**
	 * @param minScore the minScore to set
	 */
	public void setMinScore(int minScore) {
		this.minScore = minScore;
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
}