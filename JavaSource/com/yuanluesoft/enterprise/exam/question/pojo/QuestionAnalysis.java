package com.yuanluesoft.enterprise.exam.question.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:试题分析(exam_question_analysis)
 * @author linchuan
 *
 */
public class QuestionAnalysis extends Record {
	private long questionId; //试题ID
	private String questionAnalysis; //错析内容
	
	/**
	 * @return the questionAnalysis
	 */
	public String getQuestionAnalysis() {
		return questionAnalysis;
	}
	/**
	 * @param questionAnalysis the questionAnalysis to set
	 */
	public void setQuestionAnalysis(String questionAnalysis) {
		this.questionAnalysis = questionAnalysis;
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