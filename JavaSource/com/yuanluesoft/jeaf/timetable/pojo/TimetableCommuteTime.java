package com.yuanluesoft.jeaf.timetable.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 上下班时间(timetable_commute_time)
 * @author linchuan
 *
 */
public class TimetableCommuteTime extends Record {
	private long timetableId; //作息时间配置ID
	private int shiftNumber; //班次
	private String description; //描述,如:上午上班
	private char isOvertime = '0'; //是否属于加班
	private String onDutyTime; //上班时间,如:08:30
	private String offDutyTime; //下班时间,如:12:00
	private String clockInBegin; //上班打卡开始时间
	private String clockInEnd; //上班打卡结束时间
	private String clockOutBegin; //下班打卡开始时间
	private String clockOutEnd; //下班打卡结束时间
	private int lateMiniutes; //迟到多长时间算缺勤,以分钟为单位
	private int earlyMiniutes; //早退多长时间算缺勤,以分钟为单位
	private double absentDay; //缺勤天数,默认0.5
	private String effectiveBegin; //启用时间,如：06-01(夏令时)
	private String effectiveEnd; //停用时间,如：10-31(夏令时)
	
	/**
	 * @return the absentDay
	 */
	public double getAbsentDay() {
		return absentDay;
	}
	/**
	 * @param absentDay the absentDay to set
	 */
	public void setAbsentDay(double absentDay) {
		this.absentDay = absentDay;
	}
	/**
	 * @return the clockInBegin
	 */
	public String getClockInBegin() {
		return clockInBegin;
	}
	/**
	 * @param clockInBegin the clockInBegin to set
	 */
	public void setClockInBegin(String clockInBegin) {
		this.clockInBegin = clockInBegin;
	}
	/**
	 * @return the clockInEnd
	 */
	public String getClockInEnd() {
		return clockInEnd;
	}
	/**
	 * @param clockInEnd the clockInEnd to set
	 */
	public void setClockInEnd(String clockInEnd) {
		this.clockInEnd = clockInEnd;
	}
	/**
	 * @return the clockOutBegin
	 */
	public String getClockOutBegin() {
		return clockOutBegin;
	}
	/**
	 * @param clockOutBegin the clockOutBegin to set
	 */
	public void setClockOutBegin(String clockOutBegin) {
		this.clockOutBegin = clockOutBegin;
	}
	/**
	 * @return the clockOutEnd
	 */
	public String getClockOutEnd() {
		return clockOutEnd;
	}
	/**
	 * @param clockOutEnd the clockOutEnd to set
	 */
	public void setClockOutEnd(String clockOutEnd) {
		this.clockOutEnd = clockOutEnd;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the earlyMiniutes
	 */
	public int getEarlyMiniutes() {
		return earlyMiniutes;
	}
	/**
	 * @param earlyMiniutes the earlyMiniutes to set
	 */
	public void setEarlyMiniutes(int earlyMiniutes) {
		this.earlyMiniutes = earlyMiniutes;
	}
	/**
	 * @return the isOvertime
	 */
	public char getIsOvertime() {
		return isOvertime;
	}
	/**
	 * @param isOvertime the isOvertime to set
	 */
	public void setIsOvertime(char isOvertime) {
		this.isOvertime = isOvertime;
	}
	/**
	 * @return the lateMiniutes
	 */
	public int getLateMiniutes() {
		return lateMiniutes;
	}
	/**
	 * @param lateMiniutes the lateMiniutes to set
	 */
	public void setLateMiniutes(int lateMiniutes) {
		this.lateMiniutes = lateMiniutes;
	}
	/**
	 * @return the offDutyTime
	 */
	public String getOffDutyTime() {
		return offDutyTime;
	}
	/**
	 * @param offDutyTime the offDutyTime to set
	 */
	public void setOffDutyTime(String offDutyTime) {
		this.offDutyTime = offDutyTime;
	}
	/**
	 * @return the onDutyTime
	 */
	public String getOnDutyTime() {
		return onDutyTime;
	}
	/**
	 * @param onDutyTime the onDutyTime to set
	 */
	public void setOnDutyTime(String onDutyTime) {
		this.onDutyTime = onDutyTime;
	}
	/**
	 * @return the timetableId
	 */
	public long getTimetableId() {
		return timetableId;
	}
	/**
	 * @param timetableId the timetableId to set
	 */
	public void setTimetableId(long timetableId) {
		this.timetableId = timetableId;
	}
	/**
	 * @return the effectiveBegin
	 */
	public String getEffectiveBegin() {
		return effectiveBegin;
	}
	/**
	 * @param effectiveBegin the effectiveBegin to set
	 */
	public void setEffectiveBegin(String effectiveBegin) {
		this.effectiveBegin = effectiveBegin;
	}
	/**
	 * @return the effectiveEnd
	 */
	public String getEffectiveEnd() {
		return effectiveEnd;
	}
	/**
	 * @param effectiveEnd the effectiveEnd to set
	 */
	public void setEffectiveEnd(String effectiveEnd) {
		this.effectiveEnd = effectiveEnd;
	}
	/**
	 * @return the shiftNumber
	 */
	public int getShiftNumber() {
		return shiftNumber;
	}
	/**
	 * @param shiftNumber the shiftNumber to set
	 */
	public void setShiftNumber(int shiftNumber) {
		this.shiftNumber = shiftNumber;
	}
}