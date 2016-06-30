package com.yuanluesoft.cms.publicservice.processor;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.publicservice.model.PublicServiceStatistic;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.processor.SiteRecordListProcessor;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class PublicServiceStatisticProcessor extends SiteRecordListProcessor {
	private DatabaseService databaseService; //数据库服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		String pojoClassName = (String)view.getExtendParameter("pojoClassName");
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
		String siteIds = getRelatedSiteIds(view, recordListModel, parentSite);
		PublicServiceStatistic statistic = new PublicServiceStatistic();
		statistic.setTotal(statistic(pojoClassName, null, null, false, siteIds)); //累计受理
		statistic.setCompleteTotal(statistic(pojoClassName, null, null, true, siteIds)); //累计办结
		Date date = DateTimeUtils.date();
		statistic.setTodayTotal(statistic(pojoClassName, date, DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, 1), false, siteIds)); //今日累计受理
		statistic.setTodayCompleteTotal(statistic(pojoClassName, date, DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, 1), true, siteIds)); //今日累计办结
		statistic.setYesterdayTotal(statistic(pojoClassName, DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, -1), date, false, siteIds)); //昨日累计受理
		statistic.setYesterdayCompleteTotal(statistic(pojoClassName, DateTimeUtils.add(date, Calendar.DAY_OF_MONTH, -1), date, true, siteIds)); //昨日累计办结
		date = DateTimeUtils.set(date, Calendar.DAY_OF_MONTH, 1);
		statistic.setMonthTotal(statistic(pojoClassName, date, DateTimeUtils.add(date, Calendar.MONTH, 1), false, siteIds)); //本月累计受理
		statistic.setMonthCompleteTotal(statistic(pojoClassName, date, DateTimeUtils.add(date, Calendar.MONTH, 1), true, siteIds)); //本月累计办结
		return new RecordListData(ListUtils.generateList(statistic), 1); 
	}
	
	/**
	 * 统计
	 * @param pojoClassName
	 * @param beginDate
	 * @param endDate
	 * @param completeOnly
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	private int statistic(String pojoClassName, Date beginDate, Date endDate, boolean completeOnly, String siteIds) throws ServiceException {
		String where = null;
		if(beginDate!=null) { //指定时间
			where =  pojoClassName + "." + (completeOnly ? "completeTime" : "created") + " >=DATE(" + DateTimeUtils.formatDate(beginDate, null) + ")" +
					 " and " + pojoClassName + "." + (completeOnly ? "completeTime" : "created") + "<DATE(" + DateTimeUtils.formatDate(endDate, null) + ")";
		}
		if(completeOnly) { //办结
			where = (where==null ? "" : where + " and ") + " not " + pojoClassName + ".completeTime is null";
		}
		if(siteIds!=null) {
			where = (where==null ? "" : where + " and ") + pojoClassName + ".siteId in (" + siteIds + ")";
		}
		String hql = "select count(" + pojoClassName + ".id)" +
					 " from " + pojoClassName + " " + pojoClassName +
					 (where==null ? "" : " where " + where);
		Number total = (Number)databaseService.findRecordByHql(hql);
		return total==null ? 0 : total.intValue();
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}