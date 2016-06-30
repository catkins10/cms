/*
 * Created on 2005-11-18
 *
 */
package com.yuanluesoft.j2oa.meeting.pojo;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 会议室(meeting_meeting_room)
 * @author linchuan
 *
 */
public class MeetingRoom extends Record {
	private String name; //会议室名称
	private int seating; //座位容量数
	private String tableAndChair; //桌椅配备情况
	private String fixture; //设备情况
	private String remark; //备注
	
	private List todoMeetings;
	
	/**
	 * 获取本会议室的会议信息
	 * @return
	 */
	public String getMeetingsInfo() {
		if(todoMeetings==null) {
			return null;
		}
		String info = null;
		int i = 1;
		SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd HH:mm");
		for(Iterator iterator = todoMeetings.iterator(); iterator.hasNext();) {
			Meeting meeting = (Meeting)iterator.next();
			info = (info==null ? "" : info + "\r\n") + (i++) + "、" + formater.format(meeting.getBeginTime()) + " 至 " + formater.format(meeting.getEndTime()) + " " + meeting.getSubject();
		}
		return info;
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
}
