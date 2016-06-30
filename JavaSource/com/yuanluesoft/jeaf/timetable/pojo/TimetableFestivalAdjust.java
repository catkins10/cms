package com.yuanluesoft.jeaf.timetable.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 调休(timetable_festival_adjust)
 * @author linchuan
 *
 */
public class TimetableFestivalAdjust extends Record {
	private long festivalId; //节假日ID
	private Timestamp beginTime; //开始时间
	private Timestamp endTime; //结束时间
	
	/**
	 * @return the beginTime
	 */
	public Timestamp getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the festivalId
	 */
	public long getFestivalId() {
		return festivalId;
	}
	/**
	 * @param festivalId the festivalId to set
	 */
	public void setFestivalId(long festivalId) {
		this.festivalId = festivalId;
	}
}