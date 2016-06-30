package com.yuanluesoft.jeaf.timetable.forms;

import com.yuanluesoft.jeaf.timetable.pojo.TimetableFestivalAdjust;

/**
 * 
 * @author linchuan
 *
 */
public class FestivalAdjust extends Festival {
	private TimetableFestivalAdjust adjust = new TimetableFestivalAdjust();

	/**
	 * @return the adjust
	 */
	public TimetableFestivalAdjust getAdjust() {
		return adjust;
	}

	/**
	 * @param adjust the adjust to set
	 */
	public void setAdjust(TimetableFestivalAdjust adjust) {
		this.adjust = adjust;
	}
}