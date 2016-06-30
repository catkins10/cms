package com.yuanluesoft.cms.advice.forms.admin;

import com.yuanluesoft.cms.advice.pojo.AdviceTopic;
import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;

/**
 * 
 * @author linchuan
 *
 */
public class Advice extends PublicServiceAdminForm {
	private long topicId; //主题ID
	private AdviceTopic topic; //主题
	
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
}