package com.yuanluesoft.cms.advice.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 民意征集:主题(cms_advice_topic)
 * @author linchuan
 *
 */
public class AdviceTopic extends Record {
	private String subject; //标题
	private String body; //内容
	private Date endDate; //截止日期
	private char issue = '0'; //是否发布
	private Timestamp issueTime; //发布时间
	private long creatorId; //创建者ID
	private String creator; //创建者
	private Timestamp created; //创建时间
	private long siteId; //站点ID
	private Set advices; //建议列表,同步删除用
	private Set feedbacks; //结果反馈
	
	/**
	 * 获取状态
	 * @return
	 */
	public String getStatus() {
		return endDate==null || !endDate.before(DateTimeUtils.date()) ? "征集中" : "征集结束";
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
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the issue
	 */
	public char getIssue() {
		return issue;
	}
	/**
	 * @param issue the issue to set
	 */
	public void setIssue(char issue) {
		this.issue = issue;
	}
	/**
	 * @return the issueTime
	 */
	public Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
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
	 * @return the advices
	 */
	public Set getAdvices() {
		return advices;
	}
	/**
	 * @param advices the advices to set
	 */
	public void setAdvices(Set advices) {
		this.advices = advices;
	}

	/**
	 * @return the feedbacks
	 */
	public Set getFeedbacks() {
		return feedbacks;
	}

	/**
	 * @param feedbacks the feedbacks to set
	 */
	public void setFeedbacks(Set feedbacks) {
		this.feedbacks = feedbacks;
	}
}
