package com.yuanluesoft.jeaf.timetable.forms;

import com.yuanluesoft.jeaf.timetable.pojo.TimetableOvertime;

/**
 * 
 * @author linchuan
 *
 */
public class Overtime extends Timetable {
	private TimetableOvertime overtime =  new TimetableOvertime();

	/**
	 * @return the overtime
	 */
	public TimetableOvertime getOvertime() {
		return overtime;
	}

	/**
	 * @param overtime the overtime to set
	 */
	public void setOvertime(TimetableOvertime overtime) {
		this.overtime = overtime;
	}
}