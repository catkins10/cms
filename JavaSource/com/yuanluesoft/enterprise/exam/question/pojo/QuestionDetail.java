package com.yuanluesoft.enterprise.exam.question.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:试题详解(exam_question_detail)
 * @author linchuan
 *
 */
public class QuestionDetail extends Record {
	private long questionId; //试题ID
	private String questionDetail; //详解内容
	
	/**
	 * @return the questionDetail
	 */
	public String getQuestionDetail() {
		return questionDetail;
	}
	/**
	 * @param questionDetail the questionDetail to set
	 */
	public void setQuestionDetail(String questionDetail) {
		this.questionDetail = questionDetail;
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