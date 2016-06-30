package com.yuanluesoft.enterprise.exam.question.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:试题答案(exam_question_answer)
 * @author linchuan
 *
 */
public class QuestionAnswer extends Record {
	private long questionId; //试题ID
	private String questionAnswer; //试题答案,多选题，答案用逗号分隔，填空题如果允许多个正确答案，用###分隔
	private char caseSensitive = 0; //区分大小写
	
	/**
	 * @return the caseSensitive
	 */
	public char getCaseSensitive() {
		return caseSensitive;
	}
	/**
	 * @param caseSensitive the caseSensitive to set
	 */
	public void setCaseSensitive(char caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	/**
	 * @return the questionAnswer
	 */
	public String getQuestionAnswer() {
		return questionAnswer;
	}
	/**
	 * @param questionAnswer the questionAnswer to set
	 */
	public void setQuestionAnswer(String questionAnswer) {
		this.questionAnswer = questionAnswer;
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