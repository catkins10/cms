package com.yuanluesoft.j2oa.personnel.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 持有证书和资质情况(personnel_certificate)
 * @author linchuan
 *
 */
public class PersonnelCertificate extends Record {
	private long employeeId; //人员ID
	private Date issueDate; //时间
	private String name; //证书或者资质名称
	private String remark; //备注
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
	 * @return the issueDate
	 */
	public Date getIssueDate() {
		return issueDate;
	}
	/**
	 * @param issueDate the issueDate to set
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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