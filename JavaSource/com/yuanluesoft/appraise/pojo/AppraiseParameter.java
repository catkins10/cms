package com.yuanluesoft.appraise.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 参数配置(appraise_parameter)
 * @author linchuan
 *
 */
public class AppraiseParameter extends Record {
	private int recipientsUploadEnd; //服务对象上报截止日期,1-28
	private int recipientsUploadUrgeBegin; //服务对象上报催办开始日期,1-28
	private int recipientsUploadUrgeDays; //服务对象上报催办天数
	private String recipientsUploadUrgeTime; //服务对象上报催办时间
	private String recipientsUploadUrgeSms; //服务对象上报催办短信格式,如:xxx单位,请上传2013年12月的管理服务对象
	private int recipientsLimit; //服务对象上限
	
	/**
	 * @return the recipientsUploadEnd
	 */
	public int getRecipientsUploadEnd() {
		return recipientsUploadEnd;
	}
	/**
	 * @param recipientsUploadEnd the recipientsUploadEnd to set
	 */
	public void setRecipientsUploadEnd(int recipientsUploadEnd) {
		this.recipientsUploadEnd = recipientsUploadEnd;
	}
	/**
	 * @return the recipientsUploadUrgeBegin
	 */
	public int getRecipientsUploadUrgeBegin() {
		return recipientsUploadUrgeBegin;
	}
	/**
	 * @param recipientsUploadUrgeBegin the recipientsUploadUrgeBegin to set
	 */
	public void setRecipientsUploadUrgeBegin(int recipientsUploadUrgeBegin) {
		this.recipientsUploadUrgeBegin = recipientsUploadUrgeBegin;
	}
	/**
	 * @return the recipientsUploadUrgeSms
	 */
	public String getRecipientsUploadUrgeSms() {
		return recipientsUploadUrgeSms;
	}
	/**
	 * @param recipientsUploadUrgeSms the recipientsUploadUrgeSms to set
	 */
	public void setRecipientsUploadUrgeSms(String recipientsUploadUrgeSms) {
		this.recipientsUploadUrgeSms = recipientsUploadUrgeSms;
	}
	/**
	 * @return the recipientsUploadUrgeTime
	 */
	public String getRecipientsUploadUrgeTime() {
		return recipientsUploadUrgeTime;
	}
	/**
	 * @param recipientsUploadUrgeTime the recipientsUploadUrgeTime to set
	 */
	public void setRecipientsUploadUrgeTime(String recipientsUploadUrgeTime) {
		this.recipientsUploadUrgeTime = recipientsUploadUrgeTime;
	}
	/**
	 * @return the recipientsUploadUrgeDays
	 */
	public int getRecipientsUploadUrgeDays() {
		return recipientsUploadUrgeDays;
	}
	/**
	 * @param recipientsUploadUrgeDays the recipientsUploadUrgeDays to set
	 */
	public void setRecipientsUploadUrgeDays(int recipientsUploadUrgeDays) {
		this.recipientsUploadUrgeDays = recipientsUploadUrgeDays;
	}
	/**
	 * @return the recipientsLimit
	 */
	public int getRecipientsLimit() {
		return recipientsLimit;
	}
	/**
	 * @param recipientsLimit the recipientsLimit to set
	 */
	public void setRecipientsLimit(int recipientsLimit) {
		this.recipientsLimit = recipientsLimit;
	}
}