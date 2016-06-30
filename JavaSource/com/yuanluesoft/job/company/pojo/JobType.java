package com.yuanluesoft.job.company.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 职位:工作性质(job_job_type)
 * @author linchuan
 *
 */
public class JobType extends Record {
	private long jobId; //职位ID
	private int type; //工作性质,全职,兼职,实习,暑期工
	
	/**
	 * @return the jobId
	 */
	public long getJobId() {
		return jobId;
	}
	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
}