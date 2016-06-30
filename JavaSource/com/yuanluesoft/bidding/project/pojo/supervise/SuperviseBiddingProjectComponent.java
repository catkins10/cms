package com.yuanluesoft.bidding.project.pojo.supervise;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 工程组成部分
 * @author linchuan
 *
 */
public class SuperviseBiddingProjectComponent extends Record {
	private long projectId; //工程ID
	private String projectName; //工程名称
	private String biddingMode; //招标方式
	private Timestamp publicBeginTime; //公示时间
	private int publicLimit; //公示期限,以天为单位
	private Timestamp publicEndTime; //公示截止时间
	private long publicPersonId; //发布人ID
	private String publicPersonName; //发布人
	private String remark; //备注
	
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the publicBeginTime
	 */
	public Timestamp getPublicBeginTime() {
		return publicBeginTime;
	}
	/**
	 * @param publicBeginTime the publicBeginTime to set
	 */
	public void setPublicBeginTime(Timestamp publicBeginTime) {
		this.publicBeginTime = publicBeginTime;
	}
	/**
	 * @return the publicEndTime
	 */
	public Timestamp getPublicEndTime() {
		return publicEndTime;
	}
	/**
	 * @param publicEndTime the publicEndTime to set
	 */
	public void setPublicEndTime(Timestamp publicEndTime) {
		this.publicEndTime = publicEndTime;
	}
	/**
	 * @return the publicPersonId
	 */
	public long getPublicPersonId() {
		return publicPersonId;
	}
	/**
	 * @param publicPersonId the publicPersonId to set
	 */
	public void setPublicPersonId(long publicPersonId) {
		this.publicPersonId = publicPersonId;
	}
	/**
	 * @return the publicPersonName
	 */
	public String getPublicPersonName() {
		return publicPersonName;
	}
	/**
	 * @param publicPersonName the publicPersonName to set
	 */
	public void setPublicPersonName(String publicPersonName) {
		this.publicPersonName = publicPersonName;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the publicLimit
	 */
	public int getPublicLimit() {
		return publicLimit;
	}
	/**
	 * @param publicLimit the publicLimit to set
	 */
	public void setPublicLimit(int publicLimit) {
		this.publicLimit = publicLimit;
	}
	/**
	 * @return the biddingMode
	 */
	public String getBiddingMode() {
		return biddingMode;
	}
	/**
	 * @param biddingMode the biddingMode to set
	 */
	public void setBiddingMode(String biddingMode) {
		this.biddingMode = biddingMode;
	}
}
