package com.yuanluesoft.enterprise.exam.learn.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 学习:题目浏览(exam_learn_question)
 * @author linchuan
 *
 */
public class LearnQuestion extends Record {
	private long personId; //用户ID
	private long questionId; //题目ID
	private Timestamp learnTime; //学习时间
	
	/**
	 * @return the learnTime
	 */
	public Timestamp getLearnTime() {
		return learnTime;
	}
	/**
	 * @param learnTime the learnTime to set
	 */
	public void setLearnTime(Timestamp learnTime) {
		this.learnTime = learnTime;
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
	 * @return the questionId
	 */
	public long getQuestionId() {
		return questionId;
	}
	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
}