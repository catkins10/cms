package com.yuanluesoft.enterprise.exam.question.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 题库:错题举报(exam_question_wrong)
 * @author linchuan
 *
 */
public class QuestionWrong extends Record {
	private long questionId; //试题ID
	private String description; //描述
	private long informerId; //举报人ID
	private String informerName; //举报人姓名
	private Timestamp created; //举报时间
	private String transact; //处理情况
	private Timestamp transactTime; //办理时间
	private long transactorId; //办理人ID
	private String transactorName; //办理人
	
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the informerId
	 */
	public long getInformerId() {
		return informerId;
	}
	/**
	 * @param informerId the informerId to set
	 */
	public void setInformerId(long informerId) {
		this.informerId = informerId;
	}
	/**
	 * @return the informerName
	 */
	public String getInformerName() {
		return informerName;
	}
	/**
	 * @param informerName the informerName to set
	 */
	public void setInformerName(String informerName) {
		this.informerName = informerName;
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
	 * @return the transact
	 */
	public String getTransact() {
		return transact;
	}
	/**
	 * @param transact the transact to set
	 */
	public void setTransact(String transact) {
		this.transact = transact;
	}
	/**
	 * @return the transactorId
	 */
	public long getTransactorId() {
		return transactorId;
	}
	/**
	 * @param transactorId the transactorId to set
	 */
	public void setTransactorId(long transactorId) {
		this.transactorId = transactorId;
	}
	/**
	 * @return the transactorName
	 */
	public String getTransactorName() {
		return transactorName;
	}
	/**
	 * @param transactorName the transactorName to set
	 */
	public void setTransactorName(String transactorName) {
		this.transactorName = transactorName;
	}
	/**
	 * @return the transactTime
	 */
	public Timestamp getTransactTime() {
		return transactTime;
	}
	/**
	 * @param transactTime the transactTime to set
	 */
	public void setTransactTime(Timestamp transactTime) {
		this.transactTime = transactTime;
	}
}