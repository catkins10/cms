package com.yuanluesoft.jeaf.timetable.pojo;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 日常休息时间(timetable_off_day)
 * @author linchuan
 *
 */
public class TimetableOffDay extends Record {
	private long timetableId; //作息时间配置ID
	private int offDayOfWeek; //星期,如：6，表示周六
	private String beginTime; //开始时间,如：00:00
	private String endTime; //结束时间,如：00:00
	
	/**
	 * 获取开始时间
	 * @return
	 */
	public String getOffDayNameOfWeek() {
		if(offDayOfWeek==0) {
			return null;
		}
		return "星期" + (offDayOfWeek==1 ? "日" : StringUtils.getChineseNumber(offDayOfWeek-1, false));
	}

	/**
	 * @return the beginTime
	 */
	public String getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	 * @return the offDayOfWeek
	 */
	public int getOffDayOfWeek() {
		return offDayOfWeek;
	}

	/**
	 * @param offDayOfWeek the offDayOfWeek to set
	 */
	public void setOffDayOfWeek(int offDayOfWeek) {
		this.offDayOfWeek = offDayOfWeek;
	}
}