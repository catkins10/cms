package com.yuanluesoft.job.apply.forms;

import java.sql.Timestamp;

import com.yuanluesoft.job.apply.pojo.JobApplyInvitation;

/**
 * 
 * @author linchuan
 *
 */
public class Invitation extends Apply {
	private JobApplyInvitation invitation = new JobApplyInvitation();
	private Timestamp interviewTime; //面试时间

	/**
	 * @return the invitation
	 */
	public JobApplyInvitation getInvitation() {
		return invitation;
	}

	/**
	 * @param invitation the invitation to set
	 */
	public void setInvitation(JobApplyInvitation invitation) {
		this.invitation = invitation;
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
}