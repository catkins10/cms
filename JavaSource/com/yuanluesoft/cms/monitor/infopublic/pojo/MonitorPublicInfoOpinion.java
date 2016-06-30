package com.yuanluesoft.cms.monitor.infopublic.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.cms.monitor.pojo.MonitorRecord;

/**
 * 监察:信息公开意见箱(monitor_info_opinion)
 * @author linchuan
 *
 */
public class MonitorPublicInfoOpinion  extends MonitorRecord {
	private String subject; //主题
	private String body; //正文
	private Timestamp created; //创建时间
	private String creator; //创建人姓名
	private String creatorTel; //联系电话
	private String creatorMail; //邮箱
	private String creatorIP; //创建人IP
	private String creatorMobile; //创建人手机
	private String opinion; //办理意见
	private Timestamp transactTime; //办理时间
	private String transactor; //经办人
	
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
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
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorIP
	 */
	public String getCreatorIP() {
		return creatorIP;
	}
	/**
	 * @param creatorIP the creatorIP to set
	 */
	public void setCreatorIP(String creatorIP) {
		this.creatorIP = creatorIP;
	}
	/**
	 * @return the creatorMail
	 */
	public String getCreatorMail() {
		return creatorMail;
	}
	/**
	 * @param creatorMail the creatorMail to set
	 */
	public void setCreatorMail(String creatorMail) {
		this.creatorMail = creatorMail;
	}
	/**
	 * @return the creatorMobile
	 */
	public String getCreatorMobile() {
		return creatorMobile;
	}
	/**
	 * @param creatorMobile the creatorMobile to set
	 */
	public void setCreatorMobile(String creatorMobile) {
		this.creatorMobile = creatorMobile;
	}
	/**
	 * @return the creatorTel
	 */
	public String getCreatorTel() {
		return creatorTel;
	}
	/**
	 * @param creatorTel the creatorTel to set
	 */
	public void setCreatorTel(String creatorTel) {
		this.creatorTel = creatorTel;
	}
	/**
	 * @return the opinion
	 */
	public String getOpinion() {
		return opinion;
	}
	/**
	 * @param opinion the opinion to set
	 */
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the transactor
	 */
	public String getTransactor() {
		return transactor;
	}
	/**
	 * @param transactor the transactor to set
	 */
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	/**
	 * @return the transactTime
	 */
	public Timestamp getTransactTime() {
		return transactTime;
	}
	/**
	 * @param transactTime the transactTime to set
	 */
	public void setTransactTime(Timestamp transactTime) {
		this.transactTime = transactTime;
	}
}