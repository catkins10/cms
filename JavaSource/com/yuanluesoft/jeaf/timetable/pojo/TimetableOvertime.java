package com.yuanluesoft.jeaf.timetable.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 加班时长设置(timetable_overtime)
 * @author linchuan
 *
 */
public class TimetableOvertime extends Record {
	private long timetableId; //作息时间配置ID
	private double onDutyTime; //加班小时数
	private double absentDay; //换算的加班天数
	
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
	 * @return the onDutyTime
	 */
	public double getOnDutyTime() {
		return onDutyTime;
	}
	/**
	 * @param onDutyTime the onDutyTime to set
	 */
	public void setOnDutyTime(double onDutyTime) {
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
}