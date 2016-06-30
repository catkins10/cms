package com.yuanluesoft.cms.advice.pojo;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 民意征集(cms_advice)
 * @author linchuan
 *
 */
public class Advice extends PublicService {
	private long topicId; //主题ID
	private AdviceTopic topic; //主题

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
	 * @return the topic
	 */
	public AdviceTopic getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(AdviceTopic topic) {
		this.topic = topic;
	}
}
