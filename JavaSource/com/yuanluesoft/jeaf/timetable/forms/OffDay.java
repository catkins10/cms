package com.yuanluesoft.jeaf.timetable.forms;

import com.yuanluesoft.jeaf.timetable.pojo.TimetableOffDay;

/**
 * 
 * @author linchuan
 *
 */
public class OffDay extends Timetable {
	private TimetableOffDay offDay =  new TimetableOffDay();
	
	/**
	 * @return the offDay
	 */
	public TimetableOffDay getOffDay() {
		return offDay;
	}

	/**
	 * @param offDay the offDay to set
	 */
	public void setOffDay(TimetableOffDay offDay) {
		this.offDay = offDay;
	}
}