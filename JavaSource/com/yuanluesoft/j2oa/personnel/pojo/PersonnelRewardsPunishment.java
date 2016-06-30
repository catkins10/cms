package com.yuanluesoft.j2oa.personnel.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 奖惩情况(personnel_rewards_punishment)
 * @author linchuan
 *
 */
public class PersonnelRewardsPunishment extends Record {
	private long employeeId; //人员ID
	private Date happenDate; //时间
	private String content; //奖惩内容
	private String reason; //奖惩原因
	private String remark; //备注
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the employeeId
	 */
	public long getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the happenDate
	 */
	public Date getHappenDate() {
		return happenDate;
	}
	/**
	 * @param happenDate the happenDate to set
	 */
	public void setHappenDate(Date happenDate) {
		this.happenDate = happenDate;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
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
}