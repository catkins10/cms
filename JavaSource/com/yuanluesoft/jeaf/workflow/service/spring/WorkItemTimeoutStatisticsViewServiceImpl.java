package com.yuanluesoft.jeaf.workflow.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.leadermail.pojo.LeaderMail;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl;
import com.yuanluesoft.jeaf.workflow.pojo.WorkItemTimeout;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 
 * @author linchuan
 *
 */
public class WorkItemTimeoutStatisticsViewServiceImpl extends StatisticViewServiceImpl {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		//设置主记录
		for(Iterator iterator = records==null ? null : records.iterator(); iterator!=null && iterator.hasNext();) {
			WorkItemTimeout workItemTimeout = (WorkItemTimeout)iterator.next();
			WorkItemTimeout sameWorkItemTimeout = (WorkItemTimeout)ListUtils.findObjectByProperty(records, "recordId", new Long(workItemTimeout.getRecordId()));
			if(sameWorkItemTimeout!=null && sameWorkItemTimeout.getRecord()!=null) {
				workItemTimeout.setRecord(sameWorkItemTimeout.getRecord());
			}
			else {
				workItemTimeout.setRecord((WorkflowData)getDatabaseService().findRecordById(LeaderMail.class.getName(), workItemTimeout.getRecordId()));
			}
		}
		return records;
	}
}