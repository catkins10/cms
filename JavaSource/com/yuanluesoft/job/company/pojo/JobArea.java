package com.yuanluesoft.job.company.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 职位:工作地点(job_job_area)
 * @author linchuan
 *
 */
public class JobArea extends Record {
	private long jobId; //职位ID
	private long areaId; //工作地点ID
	private String area; //工作地点
	
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
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
}