package com.yuanluesoft.jeaf.timetable.forms;

import com.yuanluesoft.jeaf.timetable.pojo.TimetableCommuteTime;

/**
 * 
 * @author linchuan
 *
 */
public class CommuteTime extends Timetable {
	private TimetableCommuteTime commuteTime = new TimetableCommuteTime();

	/**
	 * @return the commuteTime
	 */
	public TimetableCommuteTime getCommuteTime() {
		return commuteTime;
	}

	/**
	 * @param commuteTime the commuteTime to set
	 */
	public void setCommuteTime(TimetableCommuteTime commuteTime) {
		this.commuteTime = commuteTime;
	}
}