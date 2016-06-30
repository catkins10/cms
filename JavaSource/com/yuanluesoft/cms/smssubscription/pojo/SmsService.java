package com.yuanluesoft.cms.smssubscription.pojo;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 短信服务(sms_service)
 * @author linchuan
 *
 */
public class SmsService extends Record {
	private long siteId; //隶属站点ID
	private String contentServiceName; //短信内容服务名称,如:publicService
	private String contentName; //短信内容名称,如:公众服务受理情况查询
	private String description; //描述
	private String subscribePrefixRule; //订阅规则:前缀,如:718,必须唯一
	private String subscribeBodyRule; //订阅规则:内容部分,如:<受理编号>#<密码>
	private String unsubscribeRule; //退订规则,即时消息不需要设置,必须唯一
	private int chargePeriod; //计费周期,以天为单位,即时消息不需要设置,目前不使用
	private double price; //价格,目前不使用
	private char isValid = '0'; //是否生效

	/**
	 * 获取完整的订阅规则
	 * @return
	 */
	public String getSubscribeRule() {
		return (subscribePrefixRule==null ? "" : subscribePrefixRule) + (subscribeBodyRule==null ? "" : subscribeBodyRule);
	}
	
	/**
	 * @return the chargePeriod
	 */
	public int getChargePeriod() {
		return chargePeriod;
	}
	/**
	 * @param chargePeriod the chargePeriod to set
	 */
	public void setChargePeriod(int chargePeriod) {
		this.chargePeriod = chargePeriod;
	}
	/**
	 * @return the contentName
	 */
	public String getContentName() {
		return contentName;
	}
	/**
	 * @param contentName the contentName to set
	 */
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	/**
	 * @return the contentServiceName
	 */
	public String getContentServiceName() {
		return contentServiceName;
	}
	/**
	 * @param contentServiceName the contentServiceName to set
	 */
	public void setContentServiceName(String contentServiceName) {
		this.contentServiceName = contentServiceName;
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
	 * @return the isValid
	 */
	public char getIsValid() {
		return isValid;
	}
	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(char isValid) {
		this.isValid = isValid;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the subscribeBodyRule
	 */
	public String getSubscribeBodyRule() {
		return subscribeBodyRule;
	}
	/**
	 * @param subscribeBodyRule the subscribeBodyRule to set
	 */
	public void setSubscribeBodyRule(String subscribeBodyRule) {
		this.subscribeBodyRule = subscribeBodyRule;
	}
	/**
	 * @return the subscribePrefixRule
	 */
	public String getSubscribePrefixRule() {
		return subscribePrefixRule;
	}
	/**
	 * @param subscribePrefixRule the subscribePrefixRule to set
	 */
	public void setSubscribePrefixRule(String subscribePrefixRule) {
		this.subscribePrefixRule = subscribePrefixRule;
	}
	/**
	 * @return the unsubscribeRule
	 */
	public String getUnsubscribeRule() {
		return unsubscribeRule;
	}
	/**
	 * @param unsubscribeRule the unsubscribeRule to set
	 */
	public void setUnsubscribeRule(String unsubscribeRule) {
		this.unsubscribeRule = unsubscribeRule;
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
}
