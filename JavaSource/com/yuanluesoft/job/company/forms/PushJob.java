package com.yuanluesoft.job.company.forms;

import com.yuanluesoft.job.company.pojo.JobPush;

/**
 * 
 * @author chuan
 *
 */
public class PushJob extends Job {
	private JobPush jobPush = new JobPush(); //职位推送
	private String mailSubject; //邮件标题
	private String mailContent; //邮件内容
	private int range; //推送范围,0/全部,1/指定人员
	
	/**
	 * @return the jobPush
	 */
	public JobPush getJobPush() {
		return jobPush;
	}
	/**
	 * @param jobPush the jobPush to set
	 */
	public void setJobPush(JobPush jobPush) {
		this.jobPush = jobPush;
	}
	/**
	 * @return the mailContent
	 */
	public String getMailContent() {
		return mailContent;
	}
	/**
	 * @param mailContent the mailContent to set
	 */
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	/**
	 * @return the range
	 */
	public int getRange() {
		return range;
	}
	/**
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}
	/**
	 * @return the mailSubject
	 */
	public String getMailSubject() {
		return mailSubject;
	}
	/**
	 * @param mailSubject the mailSubject to set
	 */
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
}