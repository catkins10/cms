package com.yuanluesoft.enterprise.exam.question.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:解题提示(exam_question_hint)
 * @author linchuan
 *
 */
public class QuestionHint extends Record {
	private long questionId; //试题ID
	private String questionHint; //解题提示
	
	/**
	 * @return the questionHint
	 */
	public String getQuestionHint() {
		return questionHint;
	}
	/**
	 * @param questionHint the questionHint to set
	 */
	public void setQuestionHint(String questionHint) {
		this.questionHint = questionHint;
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