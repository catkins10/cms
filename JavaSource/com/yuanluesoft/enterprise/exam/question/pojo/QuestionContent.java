package com.yuanluesoft.enterprise.exam.question.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:试题内容(exam_question_content)
 * @author linchuan
 *
 */
public class QuestionContent extends Record {
	private long questionId; //试题ID
	private String questionContent; //试题内容
	
	/**
	 * @return the questionContent
	 */
	public String getQuestionContent() {
		return questionContent;
	}
	/**
	 * @param questionContent the questionContent to set
	 */
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
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