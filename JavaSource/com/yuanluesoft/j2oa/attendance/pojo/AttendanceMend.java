package com.yuanluesoft.j2oa.attendance.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 考勤:补卡(attendance_mend)
 * @author linchuan
 *
 */
public class AttendanceMend extends WorkflowData {
	private long personId; //补卡人ID
	private String personName; //补卡人
	private String reason; //补卡原因
	private char mendType = '0'; //补卡类型,0/上班,1/下班,2/加班
	private char pass = '0'; //是否审批通过
	private Timestamp mendTime; //补卡的时间,如果是上下班,自动设置为上下班时间
	
	/**
	 * @return the mendTime
	 */
	public Timestamp getMendTime() {
		return mendTime;
	}
	/**
	 * @param mendTime the mendTime to set
	 */
	public void setMendTime(Timestamp mendTime) {
		this.mendTime = mendTime;
	}
	/**
	 * @return the mendType
	 */
	public char getMendType() {
		return mendType;
	}
	/**
	 * @param mendType the mendType to set
	 */
	public void setMendType(char mendType) {
		this.mendType = mendType;
	}
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
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
	 * @return the pass
	 */
	public char getPass() {
		return pass;
	}
	/**
	 * @param pass the pass to set
	 */
	public void setPass(char pass) {
		this.pass = pass;
	}
}