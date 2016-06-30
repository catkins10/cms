package com.yuanluesoft.jeaf.timetable.services;

import java.sql.Date;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface TimetableServiceListener {
	
	/**
	 * 工作日变动
	 * @param beginDate
	 * @param endDate
	 * @throws ServiceException
	 */
	public void onWorkDayChanged(Date beginDate, Date endDate) throws ServiceException;
}