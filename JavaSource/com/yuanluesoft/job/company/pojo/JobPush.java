package com.yuanluesoft.job.company.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 职位:推送(job_job_push)
 * @author linchuan
 *
 */
public class JobPush extends Record {
	private long jobId; //职位ID
	private String jobName; //职位名称
	private long companyId; //企业ID
	private String companyName; //企业名称
	private Timestamp pushTime; //推送时间
	private String receiverIds; //接收人ID
	private String receivers; //接收人
	private long pusherId; //推送人ID
	private String pusher; //推送人
	
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
	 * @return the pusher
	 */
	public String getPusher() {
		return pusher;
	}
	/**
	 * @param pusher the pusher to set
	 */
	public void setPusher(String pusher) {
		this.pusher = pusher;
	}
	/**
	 * @return the pusherId
	 */
	public long getPusherId() {
		return pusherId;
	}
	/**
	 * @param pusherId the pusherId to set
	 */
	public void setPusherId(long pusherId) {
		this.pusherId = pusherId;
	}
	/**
	 * @return the pushTime
	 */
	public Timestamp getPushTime() {
		return pushTime;
	}
	/**
	 * @param pushTime the pushTime to set
	 */
	public void setPushTime(Timestamp pushTime) {
		this.pushTime = pushTime;
	}
	/**
	 * @return the receiverIds
	 */
	public String getReceiverIds() {
		return receiverIds;
	}
	/**
	 * @param receiverIds the receiverIds to set
	 */
	public void setReceiverIds(String receiverIds) {
		this.receiverIds = receiverIds;
	}
	/**
	 * @return the receivers
	 */
	public String getReceivers() {
		return receivers;
	}
	/**
	 * @param receivers the receivers to set
	 */
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}
	/**
	 * @return the companyId
	 */
	public long getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}
	/**
	 * @param jobName the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
}