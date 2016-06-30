package com.yuanluesoft.jeaf.timetable.model;

import java.util.Map;
import java.util.Set;

/**
 * 考勤
 * @author linchuan
 *
 */
public class WorkAttendance {
	private String personName; //用户名
	private int lateTimes; //迟到次数
	private int earlyTimes; //早退次数
	private double absentDays; //缺勤天数
	private double overtimeDays; //加班天数
	private double legalOvertimeDays; //法定假日加班天数
	private double leavePostDays; //离岗天数
	private Map  leavePostDaysByType; //各离岗类型对应的天数
	private double leaveDays; //请假天数
	private double compassionateLeaveDays; //事假天数
	private double sickLeaveDays; //病假天数
	private double businesstripDays; //出差天数
	
	/**
	 * 增加离岗类型对应的天数
	 * @param type
	 * @param days
	 */
	public void addLeavePostDaysByType(String type, double days) {
		Double daysByType = (Double)leavePostDaysByType.get(type);
		daysByType = new Double(daysByType==null ? days : daysByType.doubleValue() + days);
		leavePostDaysByType.put(type, daysByType);
	}
	
	/**
	 * 获取离岗类型列表
	 * @return
	 */
	public Set getLeavePostTypes() {
		return leavePostDaysByType==null ? null : leavePostDaysByType.keySet();
	}
	
	/**
	 * 按类型获取离岗天数
	 * @param type
	 * @return
	 */
	public double getLeavePostDaysByType(String type) {
		Double daysByType = (Double)leavePostDaysByType.get(type);
		return daysByType==null ? 0 : daysByType.doubleValue();
	}
	
	/**
	 * @return the absentDays
	 */
	public double getAbsentDays() {
		return absentDays;
	}
	/**
	 * @param absentDays the absentDays to set
	 */
	public void setAbsentDays(double absentDays) {
		this.absentDays = absentDays;
	}
	/**
	 * @return the businesstripDays
	 */
	public double getBusinesstripDays() {
		return businesstripDays;
	}
	/**
	 * @param businesstripDays the businesstripDays to set
	 */
	public void setBusinesstripDays(double businesstripDays) {
		this.businesstripDays = businesstripDays;
	}
	/**
	 * @return the compassionateLeaveDays
	 */
	public double getCompassionateLeaveDays() {
		return compassionateLeaveDays;
	}
	/**
	 * @param compassionateLeaveDays the compassionateLeaveDays to set
	 */
	public void setCompassionateLeaveDays(double compassionateLeaveDays) {
		this.compassionateLeaveDays = compassionateLeaveDays;
	}
	/**
	 * @return the earlyTimes
	 */
	public int getEarlyTimes() {
		return earlyTimes;
	}
	/**
	 * @param earlyTimes the earlyTimes to set
	 */
	public void setEarlyTimes(int earlyTimes) {
		this.earlyTimes = earlyTimes;
	}
	/**
	 * @return the lateTimes
	 */
	public int getLateTimes() {
		return lateTimes;
	}
	/**
	 * @param lateTimes the lateTimes to set
	 */
	public void setLateTimes(int lateTimes) {
		this.lateTimes = lateTimes;
	}
	/**
	 * @return the leaveDays
	 */
	public double getLeaveDays() {
		return leaveDays;
	}
	/**
	 * @param leaveDays the leaveDays to set
	 */
	public void setLeaveDays(double leaveDays) {
		this.leaveDays = leaveDays;
	}
	/**
	 * @return the legalOvertimeDays
	 */
	public double getLegalOvertimeDays() {
		return legalOvertimeDays;
	}
	/**
	 * @param legalOvertimeDays the legalOvertimeDays to set
	 */
	public void setLegalOvertimeDays(double legalOvertimeDays) {
		this.legalOvertimeDays = legalOvertimeDays;
	}
	/**
	 * @return the overtimeDays
	 */
	public double getOvertimeDays() {
		return overtimeDays;
	}
	/**
	 * @param overtimeDays the overtimeDays to set
	 */
	public void setOvertimeDays(double overtimeDays) {
		this.overtimeDays = overtimeDays;
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
	 * @return the sickLeaveDays
	 */
	public double getSickLeaveDays() {
		return sickLeaveDays;
	}
	/**
	 * @param sickLeaveDays the sickLeaveDays to set
	 */
	public void setSickLeaveDays(double sickLeaveDays) {
		this.sickLeaveDays = sickLeaveDays;
	}
	/**
	 * @return the leavePostDays
	 */
	public double getLeavePostDays() {
		return leavePostDays;
	}
	/**
	 * @param leavePostDays the leavePostDays to set
	 */
	public void setLeavePostDays(double leavePostDays) {
		this.leavePostDays = leavePostDays;
	}
}