package com.yuanluesoft.cms.smssubscription.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 短信订阅记录(sms_subscription)
 * @author linchuan
 *
 */
public class SmsSubscription extends Record {
	private long siteId; //隶属站点ID
	private String subscriberNumber; //订阅人号码
	private long serviceId; //订阅的服务ID
	private String contentName; //订阅的内容名称
	private String subscribeParameter; //订阅参数
	private Timestamp subscribeTime; //订阅时间
	private Timestamp unsubscribeTime; //退订时间
	private Timestamp endTime; //订阅失效时间,由系统设置
	
	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the serviceId
	 */
	public long getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * @return the subscriberNumber
	 */
	public String getSubscriberNumber() {
		return subscriberNumber;
	}
	/**
	 * @param subscriberNumber the subscriberNumber to set
	 */
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}
	/**
	 * @return the subscribeTime
	 */
	public Timestamp getSubscribeTime() {
		return subscribeTime;
	}
	/**
	 * @param subscribeTime the subscribeTime to set
	 */
	public void setSubscribeTime(Timestamp subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	/**
	 * @return the unsubscribeTime
	 */
	public Timestamp getUnsubscribeTime() {
		return unsubscribeTime;
	}
	/**
	 * @param unsubscribeTime the unsubscribeTime to set
	 */
	public void setUnsubscribeTime(Timestamp unsubscribeTime) {
		this.unsubscribeTime = unsubscribeTime;
	}
	/**
	 * @return the serviceName
	 */
	public String getContentName() {
		return contentName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setContentName(String serviceName) {
		this.contentName = serviceName;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the subscribeParameter
	 */
	public String getSubscribeParameter() {
		return subscribeParameter;
	}
	/**
	 * @param subscribeParameter the subscribeParameter to set
	 */
	public void setSubscribeParameter(String subscribeParameter) {
		this.subscribeParameter = subscribeParameter;
	}
}
