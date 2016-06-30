package com.yuanluesoft.cms.inquiry.pojo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 调查主题(cms_inquiry_subject)
 * @author linchuan
 *
 */
public class InquirySubject extends Record {
	private String subject; //主题
	private String description; //说明
	private Timestamp endTime; //调查截止时间
	private char isAnonymous = '1'; //匿名投票
	private char publishResult = '2'; //投票结果公示,0/不对外公开,1/投票结束后公开,2/总是公开
	private char isQuestionnaire = '0'; //是否问卷
	private char ipRestriction = '0'; //IP限制,0/不限制,1/指定时间内限制,2/一直限制
	private int ipRestrictionHours; //IP限制时间
	private char isPublic = '0'; //是否发布
	private long siteId; //站点ID
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private Set inquiries; //调查列表
	private Set feedbacks; //结果反馈
	
	/**
	 * 获取状态
	 * @return
	 */
	public String getStatus() {
		return endTime==null || !endTime.before(DateTimeUtils.now()) ? "调查中" : "调查结束";
	}
	
	/**
	 * 获取选项列表
	 * @return
	 */
	public Set getOptions() {
		try {
			return ((Inquiry)inquiries.iterator().next()).getOptions();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取投票结果
	 * @return
	 */
	public List getResults() {
		try {
			return ((Inquiry)inquiries.iterator().next()).getResults();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取结果反馈(文本格式)
	 * @return
	 */
	public String getFeedback() {
		try {
			return ((InquiryFeedback)feedbacks.iterator().next()).getFeedback();
		}
		catch(Exception e) {
			return null;
		}
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
	 * @return the isAnonymous
	 */
	public char getIsAnonymous() {
		return isAnonymous;
	}
	/**
	 * @param isAnonymous the isAnonymous to set
	 */
	public void setIsAnonymous(char isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

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
	 * @return the publishResult
	 */
	public char getPublishResult() {
		return publishResult;
	}

	/**
	 * @param publishResult the publishResult to set
	 */
	public void setPublishResult(char publishResult) {
		this.publishResult = publishResult;
	}

	/**
	 * @return the inquiries
	 */
	public Set getInquiries() {
		return inquiries;
	}

	/**
	 * @param inquiries the inquiries to set
	 */
	public void setInquiries(Set inquiries) {
		this.inquiries = inquiries;
	}
	/**
	 * @return the isPublic
	 */
	public char getIsPublic() {
		return isPublic;
	}
	/**
	 * @param isPublic the isPublic to set
	 */
	public void setIsPublic(char isPublic) {
		this.isPublic = isPublic;
	}
	/**
	 * @return the isQuestionnaire
	 */
	public char getIsQuestionnaire() {
		return isQuestionnaire;
	}
	/**
	 * @param isQuestionnaire the isQuestionnaire to set
	 */
	public void setIsQuestionnaire(char isQuestionnaire) {
		this.isQuestionnaire = isQuestionnaire;
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

	/**
	 * @return the ipRestriction
	 */
	public char getIpRestriction() {
		return ipRestriction;
	}

	/**
	 * @param ipRestriction the ipRestriction to set
	 */
	public void setIpRestriction(char ipRestriction) {
		this.ipRestriction = ipRestriction;
	}

	/**
	 * @return the ipRestrictionHours
	 */
	public int getIpRestrictionHours() {
		return ipRestrictionHours;
	}

	/**
	 * @param ipRestrictionHours the ipRestrictionHours to set
	 */
	public void setIpRestrictionHours(int ipRestrictionHours) {
		this.ipRestrictionHours = ipRestrictionHours;
	}
}
