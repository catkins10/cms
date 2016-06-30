package com.yuanluesoft.j2oa.meeting.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author LinChuan
*
 */
public class MeetingRoom extends ActionForm {
	private java.lang.String name;
	private java.lang.String fixture;
	private java.lang.String remark;
	private int seating;
	private java.lang.String tableAndChair;
	private List todoMeetings;
	private String meetingsInfo;

	/**
	 * @return Returns the fixture.
	 */
	public java.lang.String getFixture() {
		return fixture;
	}
	/**
	 * @param fixture The fixture to set.
	 */
	public void setFixture(java.lang.String fixture) {
		this.fixture = fixture;
	}
	/**
	 * @return Returns the name.
	 */
	public java.lang.String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}
	/**
	 * @return Returns the remark.
	 */
	public java.lang.String getRemark() {
		return remark;
	}
	/**
	 * @param remark The remark to set.
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	/**
	 * @return Returns the seating.
	 */
	public int getSeating() {
		return seating;
	}
	/**
	 * @param seating The seating to set.
	 */
	public void setSeating(int seating) {
		this.seating = seating;
	}
	/**
	 * @return Returns the tableAndChair.
	 */
	public java.lang.String getTableAndChair() {
		return tableAndChair;
	}
	/**
	 * @param tableAndChair The tableAndChair to set.
	 */
	public void setTableAndChair(java.lang.String tableAndChair) {
		this.tableAndChair = tableAndChair;
	}
	/**
	 * @return Returns the todoMeetings.
	 */
	public List getTodoMeetings() {
		return todoMeetings;
	}
	/**
	 * @param todoMeetings The todoMeetings to set.
	 */
	public void setTodoMeetings(List todoMeetings) {
		this.todoMeetings = todoMeetings;
	}
	/**
	 * @return Returns the meetingsInfo.
	 */
	public String getMeetingsInfo() {
		return meetingsInfo;
	}
	/**
	 * @param meetingsInfo The meetingsInfo to set.
	 */
	public void setMeetingsInfo(String meetingsInfo) {
		this.meetingsInfo = meetingsInfo;
	}
}