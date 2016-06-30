/**
 * 
 */
package com.yuanluesoft.jeaf.stat.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 学生登录统计(stat_student_login)
 * @author linchuan
 *
 */
public class StudentLoginStat extends Record {
	private long personId; //用户ID
	private String personName; //姓名
	private long times; //登录次数
	private Date loginDate; //日期
	
	/**
	 * @return the loginDate
	 */
	public Date getLoginDate() {
		return loginDate;
	}
	/**
	 * @param loginDate the loginDate to set
	 */
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
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
	 * @return the times
	 */
	public long getTimes() {
		return times;
	}
	/**
	 * @param times the times to set
	 */
	public void setTimes(long times) {
		this.times = times;
	}
}