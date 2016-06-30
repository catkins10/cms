package com.yuanluesoft.enterprise.exam.pojo;

import com.yuanluesoft.enterprise.exam.question.pojo.Question;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:出题(exam_paper_question)
 * @author linchuan
 *
 */
public class ExamPaperQuestion extends Record {
	private long paperId; //考卷ID
	private long questionId; //题目ID
	private double questionScore; //题目分值
	
	//扩展属性
	private Question question; //题目
	private ExamTestAnswer examTestAnswer; //作答
	
	/**
	 * @return the paperId
	 */
	public long getPaperId() {
		return paperId;
	}
	/**
	 * @param paperId the paperId to set
	 */
	public void setPaperId(long paperId) {
		this.paperId = paperId;
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
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}
	/**
	 * @return the examTestAnswer
	 */
	public ExamTestAnswer getExamTestAnswer() {
		return examTestAnswer;
	}
	/**
	 * @param examTestAnswer the examTestAnswer to set
	 */
	public void setExamTestAnswer(ExamTestAnswer examTestAnswer) {
		this.examTestAnswer = examTestAnswer;
	}
}