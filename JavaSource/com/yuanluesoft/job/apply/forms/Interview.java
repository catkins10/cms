package com.yuanluesoft.job.apply.forms;

import com.yuanluesoft.job.apply.pojo.JobApplyInterview;

/**
 * 
 * @author linchuan
 *
 */
public class Interview extends Apply {
	private JobApplyInterview interview = new JobApplyInterview();

	/**
	 * @return the interview
	 */
	public JobApplyInterview getInterview() {
		return interview;
	}

	/**
	 * @param interview the interview to set
	 */
	public void setInterview(JobApplyInterview interview) {
		this.interview = interview;
	}
}