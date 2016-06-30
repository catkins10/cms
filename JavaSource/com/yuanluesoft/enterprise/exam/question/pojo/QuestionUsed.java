package com.yuanluesoft.enterprise.exam.question.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:用户使用过的题目(exam_question_used)
 * @author linchuan
 *
 */
public class QuestionUsed extends Record {
	private long personId; //用户ID
	private long questionId; //题目ID
	private Timestamp useTime; //使用时间
	
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
	/**
	 * @return the useTime
	 */
	public Timestamp getUseTime() {
		return useTime;
	}
	/**
	 * @param useTime the useTime to set
	 */
	public void setUseTime(Timestamp useTime) {
		this.useTime = useTime;
	}
}