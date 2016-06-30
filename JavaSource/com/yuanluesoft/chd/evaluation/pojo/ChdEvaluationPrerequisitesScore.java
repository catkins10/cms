package com.yuanluesoft.chd.evaluation.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 必备条件:评价等级对应的结果(chd_eval_prerequisites_score)
 * @author linchuan
 *
 */
public class ChdEvaluationPrerequisitesScore extends Record {
	private long prerequisitesId; //必备条件ID
	private long levelId; //评价等级ID
	private String level; //评价等级,一星级、二星级、三星级、四星级、五星级、省一星级...
	private String score; //对应的结果
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
	 * @return the score
	 */
	public String getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	/**
	 * @return the prerequisitesId
	 */
	public long getPrerequisitesId() {
		return prerequisitesId;
	}
	/**
	 * @param prerequisitesId the prerequisitesId to set
	 */
	public void setPrerequisitesId(long prerequisitesId) {
		this.prerequisitesId = prerequisitesId;
	}
}