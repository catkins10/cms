package com.yuanluesoft.jeaf.timetable.pojo;

import java.sql.Date;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 作息时间(timetable_timetable)
 * @author linchuan
 *
 */
public class Timetable extends Record {
	private String description; //描述,如:设计部上下班时间
	private Date effectiveDate; //生效时间
	private float priority; //优先级
	private Set visitors; //适用的用户列表
	private Set commuteTimes; //上下班时间列表
	private Set offDays; //休息日列表
	private Set festivals; //节日列表
	private Set overtimes; //加班时长配置列表
	
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
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the commuteTimes
	 */
	public Set getCommuteTimes() {
		return commuteTimes;
	}
	/**
	 * @param commuteTimes the commuteTimes to set
	 */
	public void setCommuteTimes(Set commuteTimes) {
		this.commuteTimes = commuteTimes;
	}
	/**
	 * @return the festivals
	 */
	public Set getFestivals() {
		return festivals;
	}
	/**
	 * @param festivals the festivals to set
	 */
	public void setFestivals(Set festivals) {
		this.festivals = festivals;
	}
	/**
	 * @return the offDays
	 */
	public Set getOffDays() {
		return offDays;
	}
	/**
	 * @param offDays the offDays to set
	 */
	public void setOffDays(Set offDays) {
		this.offDays = offDays;
	}
	/**
	 * @return the overtimes
	 */
	public Set getOvertimes() {
		return overtimes;
	}
	/**
	 * @param overtimes the overtimes to set
	 */
	public void setOvertimes(Set overtimes) {
		this.overtimes = overtimes;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
}