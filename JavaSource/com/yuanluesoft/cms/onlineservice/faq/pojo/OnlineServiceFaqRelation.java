package com.yuanluesoft.cms.onlineservice.faq.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 相关问题(onlineservice_faq_relation)
 * @author linchuan
 *
 */
public class OnlineServiceFaqRelation  extends Record {
	private long faqId; //FAQID
	private long relationFaqId; //相关问题ID
	
	/**
	 * @return the faqId
	 */
	public long getFaqId() {
		return faqId;
	}
	/**
	 * @param faqId the faqId to set
	 */
	public void setFaqId(long faqId) {
		this.faqId = faqId;
	}
	/**
	 * @return the relationFaqId
	 */
	public long getRelationFaqId() {
		return relationFaqId;
	}
	/**
	 * @param relationFaqId the relationFaqId to set
	 */
	public void setRelationFaqId(long relationFaqId) {
		this.relationFaqId = relationFaqId;
	}
}