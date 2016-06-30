package com.yuanluesoft.jeaf.timetable.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 节日(timetable_festival)
 * @author linchuan
 *
 */
public class TimetableFestival extends Record {
	private long timetableId; //作息时间配置ID
	private String name; //节日名称
	private Timestamp beginTime; //放假开始时间
	private Timestamp endTime; //放假结束时间
	private Timestamp legalBeginTime; //法定开始时间
	private Timestamp legalEndTime; //法定结束时间
	private Set adjusts; //调休列表
	
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
	 * @return the legalBeginTime
	 */
	public Timestamp getLegalBeginTime() {
		return legalBeginTime;
	}
	/**
	 * @param legalBeginTime the legalBeginTime to set
	 */
	public void setLegalBeginTime(Timestamp legalBeginTime) {
		this.legalBeginTime = legalBeginTime;
	}
	/**
	 * @return the legalEndTime
	 */
	public Timestamp getLegalEndTime() {
		return legalEndTime;
	}
	/**
	 * @param legalEndTime the legalEndTime to set
	 */
	public void setLegalEndTime(Timestamp legalEndTime) {
		this.legalEndTime = legalEndTime;
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
	 * @return the adjusts
	 */
	public Set getAdjusts() {
		return adjusts;
	}
	/**
	 * @param adjusts the adjusts to set
	 */
	public void setAdjusts(Set adjusts) {
		this.adjusts = adjusts;
	}
}