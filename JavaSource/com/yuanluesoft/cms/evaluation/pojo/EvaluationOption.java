package com.yuanluesoft.cms.evaluation.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 民主测评：选择项(evaluation_option)
 * @author linchuan
 *
 */
public class EvaluationOption extends Record {
	private long topicId; //测评主题ID
	private String name; //选项名称,如：好、较好、一般、差
	private String description; //描述
	private int score; //选中时对应的分数,百分制
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the topicId
	 */
	public long getTopicId() {
		return topicId;
	}
	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}
}