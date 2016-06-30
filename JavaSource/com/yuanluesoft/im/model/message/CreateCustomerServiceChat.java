package com.yuanluesoft.im.model.message;

/**
 * 创建客服对话
 * @author linchuan
 *
 */
public class CreateCustomerServiceChat extends Message {
	private long specialistId; //指定的客服人员ID
	private long siteId; //站点ID
	
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
	 * @return the specialistId
	 */
	public long getSpecialistId() {
		return specialistId;
	}
	/**
	 * @param specialistId the specialistId to set
	 */
	public void setSpecialistId(long specialistId) {
		this.specialistId = specialistId;
	}
}