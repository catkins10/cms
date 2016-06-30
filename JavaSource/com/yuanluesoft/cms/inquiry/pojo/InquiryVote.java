package com.yuanluesoft.cms.inquiry.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 投票(cms_inquiry_vote)
 * @author linchuan
 *
 */
public class InquiryVote extends Record {
	private long optionId; //选项ID
	private String ip; //IP
	private String voter; //投票人姓名
	private long voterId; //投票人ID
	private String supplement; //补充说明
	private Timestamp created; //投票时间
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the optionId
	 */
	public long getOptionId() {
		return optionId;
	}
	/**
	 * @param optionId the optionId to set
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}
	/**
	 * @return the voter
	 */
	public String getVoter() {
		return voter;
	}
	/**
	 * @param voter the voter to set
	 */
	public void setVoter(String voter) {
		this.voter = voter;
	}
	/**
	 * @return the voterId
	 */
	public long getVoterId() {
		return voterId;
	}
	/**
	 * @param voterId the voterId to set
	 */
	public void setVoterId(long voterId) {
		this.voterId = voterId;
	}
	/**
	 * @return the supplement
	 */
	public String getSupplement() {
		return supplement;
	}
	/**
	 * @param supplement the supplement to set
	 */
	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}
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
}