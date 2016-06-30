package com.yuanluesoft.cms.onlineservice.interactive.accept.service.spring;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.interactive.accept.model.AcceptStatistic;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineServiceAcceptStatisticServiceImpl extends ViewServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		AcceptStatistic acceptStatistic = new AcceptStatistic();
		acceptStatistic.setAcceptTotal(acceptStatistic(null, null, false)); //累计受理
		acceptStatistic.setCompleteTotal(acceptStatistic(null, null, true)); //累计办结
		Date date = DateTimeUtils.date();
		acceptStatistic.setTodayAcceptTotal(acceptStatistic(date, DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, 1), false)); //今日累计受理
		acceptStatistic.setTodayCompleteTotal(acceptStatistic(date, DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, 1), true)); //今日累计办结
		acceptStatistic.setYesterdayAcceptTotal(acceptStatistic(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, -1), date, false)); //昨日累计受理
		acceptStatistic.setYesterdayCompleteTotal(acceptStatistic(DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, -1), date, true)); //昨日累计办结
		date = DateTimeUtils.set(date, Calendar.DAY_OF_MONTH, 1);
		acceptStatistic.setMonthAcceptTotal(acceptStatistic(date, DateTimeUtils.add(date, Calendar.MONTH, 1), false)); //本月累计受理
		acceptStatistic.setMonthCompleteTotal(acceptStatistic(date, DateTimeUtils.add(date, Calendar.MONTH, 1), true)); //本月累计办结
		return ListUtils.generateList(acceptStatistic); 
	}
	
	/**
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param completeOnly
	 * @return
	 * @throws ServiceException
	 */
	private int acceptStatistic(Date beginDate, Date endDate, boolean completeOnly) throws ServiceException {
		String hql = "select sum(OnlineServiceAccept.caseNumber)" +
					 " from OnlineServiceAccept OnlineServiceAccept" +
					 " where OnlineServiceAccept.caseAccept='1'" +
					 (completeOnly ? " and not OnlineServiceAccept.caseCompleteTime is null" : "") +
					 (beginDate==null ? "" : " and OnlineServiceAccept." + (completeOnly ? "caseCompleteTime" : "caseAcceptTime") + " >=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ") and OnlineServiceAccept." + (completeOnly ? "caseCompleteTime" : "caseAcceptTime") + "<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")");
		Number total = (Number)getDatabaseService().findRecordByHql(hql);
		return total==null ? 0 : total.intValue();
	}
}