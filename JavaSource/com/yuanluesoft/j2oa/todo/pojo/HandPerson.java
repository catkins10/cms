/*
 * Created on 2005-11-14
 *
 */
package com.yuanluesoft.j2oa.todo.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 交办事宜执行人(todo_hand_person)
 * @author linchuan
 *
 */
public class HandPerson extends Record {
	private long mainRecordId; //交办事宜ID
	private long personId; //执行人ID
	private String personName; //执行人姓名
	private String feedback; //反馈
	private Timestamp feedbackTime; //反馈时间
	
	/**
	 * @return Returns the feedback.
	 */
	public java.lang.String getFeedback() {
		return feedback;
	}
	/**
	 * @param feedback The feedback to set.
	 */
	public void setFeedback(java.lang.String feedback) {
		this.feedback = feedback;
	}
	/**
	 * @return Returns the feedbackTime.
	 */
	public java.sql.Timestamp getFeedbackTime() {
		return feedbackTime;
	}
	/**
	 * @param feedbackTime The feedbackTime to set.
	 */
	public void setFeedbackTime(java.sql.Timestamp feedbackTime) {
		this.feedbackTime = feedbackTime;
	}
	/**
	 * @return Returns the mainRecordId.
	 */
	public long getMainRecordId() {
		return mainRecordId;
	}
	/**
	 * @param mainRecordId The mainRecordId to set.
	 */
	public void setMainRecordId(long mainRecordId) {
		this.mainRecordId = mainRecordId;
	}
	/**
	 * @return Returns the personId.
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
}
