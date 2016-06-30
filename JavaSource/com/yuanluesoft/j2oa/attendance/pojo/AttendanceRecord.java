package com.yuanluesoft.j2oa.attendance.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 考勤:打卡记录(attendance_time_record)
 * @author linchuan
 *
 */
public class AttendanceRecord extends Record {
	private long personId; //用户ID
	private Timestamp punchTime; //打卡时间
	
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
	 * @return the punchTime
	 */
	public Timestamp getPunchTime() {
		return punchTime;
	}
	/**
	 * @param punchTime the punchTime to set
	 */
	public void setPunchTime(Timestamp punchTime) {
		this.punchTime = punchTime;
	}
}