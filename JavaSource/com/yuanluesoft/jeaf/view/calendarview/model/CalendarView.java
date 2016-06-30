/*
 * Created on 2006-3-24
 *
 */
package com.yuanluesoft.jeaf.view.calendarview.model;

import java.io.Serializable;

import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class CalendarView extends View implements Serializable {
	public final static String CALENDAR_MODE_MONTH = "month";
	public final static String CALENDAR_MODE_WEEK = "week";
	public final static String CALENDAR_MODE_DAY = "day";
	
	private String calendarAction; //日历操作
	private String calendarActionHideCondition; //日历操作隐藏条件
	private String calendarColumn; //日历列
	private String defaultMode; //默认的日历模式,month/week/day
	
	/**
	 * @return Returns the calendarColumn.
	 */
	public String getCalendarColumn() {
		return calendarColumn;
	}
	/**
	 * @param calendarColumn The calendarColumn to set.
	 */
	public void setCalendarColumn(String calendarColumn) {
		this.calendarColumn = calendarColumn;
	}
	/**
	 * @return Returns the defaultMode.
	 */
	public String getDefaultMode() {
		return defaultMode;
	}
	/**
	 * @param defaultMode The defaultMode to set.
	 */
	public void setDefaultMode(String defaultMode) {
		this.defaultMode = defaultMode;
	}
    /**
     * @return Returns the calendarAction.
     */
    public String getCalendarAction() {
        return calendarAction;
    }
    /**
     * @param calendarAction The calendarAction to set.
     */
    public void setCalendarAction(String calendarAction) {
        this.calendarAction = calendarAction;
    }
    /**
     * @return Returns the calendarActionHideCondition.
     */
    public String getCalendarActionHideCondition() {
        return calendarActionHideCondition;
    }
    /**
     * @param calendarActionHideCondition The calendarActionHideCondition to set.
     */
    public void setCalendarActionHideCondition(
            String calendarActionHideCondition) {
        this.calendarActionHideCondition = calendarActionHideCondition;
    }
}
