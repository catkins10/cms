package com.yuanluesoft.railway.evaluation.service.spring;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.railway.evaluation.service.RailwayEvaluationService;

/**
 * 
 * @author linchuan
 *
 */
public class RailwayEvaluationViewServiceImpl extends ViewServiceImpl {
	private RailwayEvaluationService railwayEvaluationService; //综合评价服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveViewData(com.yuanluesoft.jeaf.view.model.ViewPackage, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void retrieveViewData(ViewPackage viewPackage, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("admin/railwayEvaluation".equals(viewPackage.getView().getName())) { //综合评价
			int year = RequestUtils.getParameterIntValue(request, "year");
			int month = RequestUtils.getParameterIntValue(request, "month");
			if(year==0) {
				Date date = DateTimeUtils.add(DateTimeUtils.date(), Calendar.MONTH, -1);
				year = DateTimeUtils.getYear(date);
				month = DateTimeUtils.getMonth(date) + 1;
			}
			List records = railwayEvaluationService.listRailwayEvaluations(request.getParameter("postIds"), request.getParameter("orgIds"), year, month);
			viewPackage.setRecords(records);
			viewPackage.setRecordCount(records==null ? 0 : records.size());
			viewPackage.setRowNum(1);
			viewPackage.setPageCount(1);
			return;
		}
		super.retrieveViewData(viewPackage, beginRow, readRecordsOnly, countRecordsOnly, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecordCount(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected int retrieveRecordCount(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("myRailwayEvaluations".equals(view.getName())) { //我的综合评价成绩
			return 36;
		}
		return super.retrieveRecordCount(view, currentCategories, searchConditionList, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("myRailwayEvaluations".equals(view.getName())) { //我的综合评价成绩
			int pageRows = view.getPageRows();
			int recordCount = 36; //最多36个月
			Date date = DateTimeUtils.date();
			if(!railwayEvaluationService.isEvaluateCurrentMonth()) {
				date = DateTimeUtils.add(date, Calendar.MONTH, -1);
			}
			List records = new ArrayList();
			for(int i=beginRow; i<Math.min(beginRow + pageRows, recordCount); i++) {
				Date evaludateDate = DateTimeUtils.add(date, Calendar.MONTH, -i);
				records.add(railwayEvaluationService.getPersonalRailwayEvaluation(sessionInfo.getUserId(), DateTimeUtils.getYear(evaludateDate), DateTimeUtils.getMonth(evaludateDate) + 1));
			}
			return records;
		}
		return super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
	}

	/**
	 * @return the railwayEvaluationService
	 */
	public RailwayEvaluationService getRailwayEvaluationService() {
		return railwayEvaluationService;
	}

	/**
	 * @param railwayEvaluationService the railwayEvaluationService to set
	 */
	public void setRailwayEvaluationService(
			RailwayEvaluationService railwayEvaluationService) {
		this.railwayEvaluationService = railwayEvaluationService;
	}
}