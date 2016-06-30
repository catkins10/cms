package com.yuanluesoft.cms.advice.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 民意征集:结果反馈(cms_advice_feedback)
 * @author linchuan
 *
 */
public class AdviceFeedback extends Record {
	private long topicId; //主题ID
	private String feedback; //结果反馈
	private long creatorId; //反馈人ID
	private String creator; //反馈人
	private Timestamp created; //反馈时间
	private AdviceTopic adviceTopic; //征集主题
	
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
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}
	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	/**
	 * @return the topicId
	 */
	public long getTopicId() {
		return topicId;
	}
	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}
	/**
	 * @return the adviceTopic
	 */
	public AdviceTopic getAdviceTopic() {
		return adviceTopic;
	}
	/**
	 * @param adviceTopic the adviceTopic to set
	 */
	public void setAdviceTopic(AdviceTopic adviceTopic) {
		this.adviceTopic = adviceTopic;
	}
}