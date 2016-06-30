package com.yuanluesoft.enterprise.exam.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:题型(exam_exam_question_type)
 * @author linchuan
 *
 */
public class ExamQuestionType extends Record {
	private long examId; //考试ID
	private String questionType; //考试题型
	private int questionNumber; //题目数
	private double questionScore; //分数
	
	/**
	 * @return the examId
	 */
	public long getExamId() {
		return examId;
	}
	/**
	 * @param examId the examId to set
	 */
	public void setExamId(long examId) {
		this.examId = examId;
	}
	/**
	 * @return the questionNumber
	 */
	public int getQuestionNumber() {
		return questionNumber;
	}
	/**
	 * @param questionNumber the questionNumber to set
	 */
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	/**
	 * @return the questionScore
	 */
	public double getQuestionScore() {
		return questionScore;
	}
	/**
	 * @param questionScore the questionScore to set
	 */
	public void setQuestionScore(double questionScore) {
		this.questionScore = questionScore;
	}
	/**
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}
	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
}