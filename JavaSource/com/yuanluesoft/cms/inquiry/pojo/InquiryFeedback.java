package com.yuanluesoft.cms.inquiry.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 结果反馈(cms_inquiry_feedback)
 * @author linchuan
 *
 */
public class InquiryFeedback extends Record {
	private long subjectId; //主题ID
	private String feedback; //结果反馈
	private long creatorId; //反馈人ID
	private String creator; //反馈人
	private Timestamp created; //反馈时间
	private InquirySubject inquirySubject; //调查主题
	
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
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}
	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	/**
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}
	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * @return the inquirySubject
	 */
	public InquirySubject getInquirySubject() {
		return inquirySubject;
	}
	/**
	 * @param inquirySubject the inquirySubject to set
	 */
	public void setInquirySubject(InquirySubject inquirySubject) {
		this.inquirySubject = inquirySubject;
	}
}