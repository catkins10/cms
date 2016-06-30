package com.yuanluesoft.enterprise.exam.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.enterprise.exam.question.pojo.Question;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 考试:错题本(exam_wrong_question)
 * @author linchuan
 *
 */
public class ExamWrongQuestion extends Record {
	private long personId; //用户ID
	private long answerId; //答题记录ID
	private long questionId; //题目ID
	private Timestamp created; //加入时间
	private String reason; //答错原因
	private ExamTestAnswer testAnswer; //答题记录
	private Question question; //题目
	
	/**
	 * @return the answerId
	 */
	public long getAnswerId() {
		return answerId;
	}
	/**
	 * @param answerId the answerId to set
	 */
	public void setAnswerId(long answerId) {
		this.answerId = answerId;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
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
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the testAnswer
	 */
	public ExamTestAnswer getTestAnswer() {
		return testAnswer;
	}
	/**
	 * @param testAnswer the testAnswer to set
	 */
	public void setTestAnswer(ExamTestAnswer testAnswer) {
		this.testAnswer = testAnswer;
	}
}