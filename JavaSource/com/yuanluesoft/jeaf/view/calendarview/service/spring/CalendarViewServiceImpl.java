/*
 * Created on 2006-3-25
 *
 */
package com.yuanluesoft.jeaf.view.calendarview.service.spring;

import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.calendarview.model.CalendarView;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 * 
 */
public class CalendarViewServiceImpl extends ViewServiceImpl  implements ViewService {
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
	   	CalendarView calendarView = (CalendarView)view;
		//设置日历模式
	   	if(viewPackage.getCalendarMode()==null || viewPackage.getCalendarMode().equals("")) {
			viewPackage.setCalendarMode(calendarView.getDefaultMode());
			if(viewPackage.getCalendarMode()==null || viewPackage.getCalendarMode().equals("")) {
				viewPackage.setCalendarMode(CalendarView.CALENDAR_MODE_MONTH);
			}
		}
	   	//设置显示的时间
		Date beginDate = viewPackage.getBeginCalendarDate()==null ? DateTimeUtils.date() : viewPackage.getBeginCalendarDate();
		Date endDate;
		if(CalendarView.CALENDAR_MODE_MONTH.equals(viewPackage.getCalendarMode())) {
			beginDate = DateTimeUtils.set(beginDate, Calendar.DAY_OF_MONTH, 1);
			endDate = DateTimeUtils.add(beginDate, Calendar.MONTH, 1);
		}
		else if(CalendarView.CALENDAR_MODE_WEEK.equals(viewPackage.getCalendarMode())) {
			int week = DateTimeUtils.getWeek(beginDate);
			beginDate = DateTimeUtils.add(beginDate, Calendar.DAY_OF_MONTH, (week==1 ? -6 : 2 - week));
			endDate = DateTimeUtils.add(beginDate, Calendar.DAY_OF_MONTH, 7);
		}
		else { //按日
			endDate = DateTimeUtils.add(beginDate, Calendar.DAY_OF_MONTH, 1);
		}
		viewPackage.setBeginCalendarDate(beginDate);
		
		String hqlBetween = " between DATE(" + DateTimeUtils.formatDate(viewPackage.getBeginCalendarDate(), null) + ") and TIMESTAMP(" + DateTimeUtils.formatDate(DateTimeUtils.add(endDate, Calendar.DAY_OF_MONTH, -1), null) + " 23:59:59)";
		//设置where子句
		String hql = calendarView.getCalendarColumn() + hqlBetween;
		String where = calendarView.getWhere();
		calendarView.setWhere(where==null ? hql : "(" + hql + ") and (" + where + ")");
		calendarView.setPageRows(0);
		super.retrieveViewPackage(viewPackage, view, beginRow, retrieveDataOnly, readRecordsOnly, countRecordsOnly, request, sessionInfo);
 		viewPackage.setRowNum(1);
	}
}