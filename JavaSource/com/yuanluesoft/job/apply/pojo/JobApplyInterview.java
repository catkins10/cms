package com.yuanluesoft.job.apply.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 面试情况(job_apply_interview)
 * @author linchuan
 *
 */
public class JobApplyInterview extends Record {
	private long applyId; //应聘ID
	private String interviewer; //面试官,面试邀请函,录用通知书
	private Timestamp interviewTime; //面试时间
	private String description; //面试情况
	private String opinion; //面试意见
	
	/**
	 * @return the applyId
	 */
	public long getApplyId() {
		return applyId;
	}
	/**
	 * @param applyId the applyId to set
	 */
	public void setApplyId(long applyId) {
		this.applyId = applyId;
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
	 * @return the interviewer
	 */
	public String getInterviewer() {
		return interviewer;
	}
	/**
	 * @param interviewer the interviewer to set
	 */
	public void setInterviewer(String interviewer) {
		this.interviewer = interviewer;
	}
	/**
	 * @return the interviewTime
	 */
	public Timestamp getInterviewTime() {
		return interviewTime;
	}
	/**
	 * @param interviewTime the interviewTime to set
	 */
	public void setInterviewTime(Timestamp interviewTime) {
		this.interviewTime = interviewTime;
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
}