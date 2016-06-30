package com.yuanluesoft.jeaf.timetable.forms;

import com.yuanluesoft.jeaf.timetable.pojo.TimetableFestival;

/**
 * 
 * @author linchuan
 *
 */
public class Festival extends Timetable {
	private TimetableFestival festival = new TimetableFestival();

	/**
	 * @return the festival
	 */
	public TimetableFestival getFestival() {
		return festival;
	}

	/**
	 * @param festival the festival to set
	 */
	public void setFestival(TimetableFestival festival) {
		this.festival = festival;
	}
}