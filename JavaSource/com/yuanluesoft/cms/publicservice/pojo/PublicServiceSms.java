package com.yuanluesoft.cms.publicservice.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公众服务:办结短信格式(cms_publicservice_sms)
 * @author linchuan
 *
 */
public class PublicServiceSms extends Record {
	private String applicationName; //应用名称
	private long siteId; //绑定的站点ID
	private String smsFormat; //短信格式
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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
	 * @return the smsFormat
	 */
	public String getSmsFormat() {
		return smsFormat;
	}
	/**
	 * @param smsFormat the smsFormat to set
	 */
	public void setSmsFormat(String smsFormat) {
		this.smsFormat = smsFormat;
	}
}