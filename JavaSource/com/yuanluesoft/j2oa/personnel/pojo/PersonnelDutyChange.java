package com.yuanluesoft.j2oa.personnel.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 岗位变动情况(personnel_duty_change)
 * @author linchuan
 *
 */
public class PersonnelDutyChange extends Record {
	private long employeeId; //人员ID
	private Date changeDate; //时间
	private String previousDuty; //历史岗位
	private String newDuty; //现在岗位
	private String changeReason; //岗位变动原因
	private String remark; //备注
	
	/**
	 * @return the changeDate
	 */
	public Date getChangeDate() {
		return changeDate;
	}
	/**
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	/**
	 * @return the changeReason
	 */
	public String getChangeReason() {
		return changeReason;
	}
	/**
	 * @param changeReason the changeReason to set
	 */
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
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
	 * @return the newDuty
	 */
	public String getNewDuty() {
		return newDuty;
	}
	/**
	 * @param newDuty the newDuty to set
	 */
	public void setNewDuty(String newDuty) {
		this.newDuty = newDuty;
	}
	/**
	 * @return the previousDuty
	 */
	public String getPreviousDuty() {
		return previousDuty;
	}
	/**
	 * @param previousDuty the previousDuty to set
	 */
	public void setPreviousDuty(String previousDuty) {
		this.previousDuty = previousDuty;
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