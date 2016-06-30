package com.yuanluesoft.im.cs.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 客服:常用回复(im_cs_reply)
 * @author linchuan
 *
 */
public class CSReply extends Record {
	private long siteId; //站点ID
	private long specialistId; //客服ID
	private String reply; //回复内容,如：您好，我是客服001号，请问有什么可以帮助您！
	
	/**
	 * @return the reply
	 */
	public String getReply() {
		return reply;
	}
	/**
	 * @param reply the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
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