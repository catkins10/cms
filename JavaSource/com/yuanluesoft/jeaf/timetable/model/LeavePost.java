package com.yuanluesoft.jeaf.timetable.model;

import java.sql.Timestamp;

/**
 * 离岗记录,如:请假、出差、培训
 * @author linchuan
 *
 */
public class LeavePost {
	private String type; //离岗类型
	private boolean isLeave; //是否请假
	private boolean isSickLeave; //是否病假
	private boolean isCompassionateLeave; //是否事假
	private boolean isBusinesstrip; //是否出差
	private Timestamp leaveBegin; //开始时间
	private Timestamp leaveEnd; //结束时间
	
	public LeavePost(String type, boolean isLeave, boolean isSickLeave, boolean isCompassionateLeave, boolean isBusinesstrip, Timestamp leaveBegin, Timestamp leaveEnd) {
		super();
		this.type = type;
		this.isLeave = isLeave;
		this.isSickLeave = isSickLeave;
		this.isCompassionateLeave = isCompassionateLeave;
		this.isBusinesstrip = isBusinesstrip;
		this.leaveBegin = leaveBegin;
		this.leaveEnd = leaveEnd;
	}
	/**
	 * @return the isBusinesstrip
	 */
	public boolean isBusinesstrip() {
		return isBusinesstrip;
	}
	/**
	 * @param isBusinesstrip the isBusinesstrip to set
	 */
	public void setBusinesstrip(boolean isBusinesstrip) {
		this.isBusinesstrip = isBusinesstrip;
	}
	/**
	 * @return the isCompassionateLeave
	 */
	public boolean isCompassionateLeave() {
		return isCompassionateLeave;
	}
	/**
	 * @param isCompassionateLeave the isCompassionateLeave to set
	 */
	public void setCompassionateLeave(boolean isCompassionateLeave) {
		this.isCompassionateLeave = isCompassionateLeave;
	}
	/**
	 * @return the isLeave
	 */
	public boolean isLeave() {
		return isLeave;
	}
	/**
	 * @param isLeave the isLeave to set
	 */
	public void setLeave(boolean isLeave) {
		this.isLeave = isLeave;
	}
	/**
	 * @return the isSickLeave
	 */
	public boolean isSickLeave() {
		return isSickLeave;
	}
	/**
	 * @param isSickLeave the isSickLeave to set
	 */
	public void setSickLeave(boolean isSickLeave) {
		this.isSickLeave = isSickLeave;
	}
	/**
	 * @return the leaveBegin
	 */
	public Timestamp getLeaveBegin() {
		return leaveBegin;
	}
	/**
	 * @param leaveBegin the leaveBegin to set
	 */
	public void setLeaveBegin(Timestamp leaveBegin) {
		this.leaveBegin = leaveBegin;
	}
	/**
	 * @return the leaveEnd
	 */
	public Timestamp getLeaveEnd() {
		return leaveEnd;
	}
	/**
	 * @param leaveEnd the leaveEnd to set
	 */
	public void setLeaveEnd(Timestamp leaveEnd) {
		this.leaveEnd = leaveEnd;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}