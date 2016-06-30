package com.yuanluesoft.traffic.busline.processor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;
import com.yuanluesoft.traffic.busline.pojo.BusLine;
import com.yuanluesoft.traffic.busline.service.BusLineService;

/**
 * 
 * @author linchuan
 *
 */
public class BusLinesProcessor extends RecordListProcessor {
	private BusLineService busLineService; //公交线路服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		RecordListData recordListData = super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		if(recordListData==null || recordListData.getRecords()==null || recordListData.getRecords().isEmpty()) {
			return recordListData;
		}
		for(Iterator iterator = recordListData.getRecords().iterator(); iterator.hasNext();) {
			BusLine busLine = (BusLine)iterator.next();
			Date changeEndDate = busLineService.resetBusLineByInterimChange(busLine);
			if(changeEndDate!=null) {
				//设置页面有效时间
				PageUtils.setPageExpiresTime(DateTimeUtils.add(changeEndDate, Calendar.DAY_OF_MONTH, 1), request);
			}
		}
		return recordListData;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#generateSearchConditions(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected List generateSearchConditions(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		List searchConditions = super.generateSearchConditions(view, recordListModel, webDirectory, parentSite, sitePage, request);
		if(searchConditions==null) {
			searchConditions = new ArrayList();
		}
		String stationName = request.getParameter("stationName");
		if(stationName!=null) { //按完整的站点名称
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, "stations.name", "string", Condition.CONDITION_EXPRESSION_EQUAL, stationName, null));
			return searchConditions;
		}
		String queryMode = request.getParameter("queryMode");
		if("按站点名称".equals(queryMode)) {
			if((stationName = request.getParameter("key"))!=null && !stationName.isEmpty()) { //模糊查询
				searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, "stations.name", "string", Condition.CONDITION_EXPRESSION_CONTAIN, stationName, null));
			}
		}
		else if("按起讫站点".equals(queryMode)) {
			if((stationName = request.getParameter("startStationName"))!=null && !stationName.isEmpty()) { //按起讫站点
				String conditions = "(select min(BusLineStation.id)" +
									" from BusLineStation BusLineStation" +
									" where BusLineStation.name like '%" + JdbcUtils.resetQuot(stationName) + "%'" +
									" and BusLineStation.busLineId=BusLine.id) is not null";
				if((stationName = request.getParameter("finishStationName"))!=null && !stationName.isEmpty()) { //按起讫站点
					conditions += " and (select min(BusLineStation.id)" +
								  " from BusLineStation BusLineStation" +
								  " where BusLineStation.name like '%" + JdbcUtils.resetQuot(stationName) + "%'" +
								  " and BusLineStation.busLineId=BusLine.id) is not null";
				}
				searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL, conditions, null));
			}
		}
		return searchConditions;
	}

	/**
	 * @return the busLineService
	 */
	public BusLineService getBusLineService() {
		return busLineService;
	}

	/**
	 * @param busLineService the busLineService to set
	 */
	public void setBusLineService(BusLineService busLineService) {
		this.busLineService = busLineService;
	}
}