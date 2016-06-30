package com.yuanluesoft.cms.evaluation.processor;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.evaluation.pojo.EvaluationTopic;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class EvaluationTopicsProcessor extends RecordListProcessor {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		RecordListData recordListData = super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		//如果记录列表是测评中的主题,设置页面有效时间
		if(!"processingEvaluationTopics".equals(view.getName()) ||
			recordListData==null || recordListData.getRecords()==null || recordListData.getRecords().isEmpty()) {
			return recordListData;
		}
		//获取最小的征集结束时间
		Timestamp minTime = null;
		for(Iterator iterator = recordListData.getRecords().iterator(); iterator.hasNext();) {
			EvaluationTopic evaluationTopic = (EvaluationTopic)iterator.next();
			if(evaluationTopic.getEndTime()!=null && (minTime==null || minTime.after(evaluationTopic.getEndTime()))) {
				minTime = evaluationTopic.getEndTime();
			}
		}
		//设置页面有效时间
		PageUtils.setPageExpiresTime(minTime, request);
		return recordListData;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		super.createStaticPageRebuildBasisByRecordList(recordListModel, view, staticPageId, pageElement, sitePage, webDirectory, parentSite, htmlParser, databaseService, request);
		if("completedEvaluationTopics".equals(recordListModel.getRecordListName())) { //已结束的主题
			//获取最快结束的主题的结束时间
			String hql = "select EvaluationTopic.endTime" +
						 " from EvaluationTopic EvaluationTopic" +
						 " where not EvaluationTopic.endTime is null" +
						 " and EvaluationTopic.endTime>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")" +
						 " order by EvaluationTopic.endTime";
			//设置页面有效时间
			PageUtils.setPageExpiresTime((Timestamp)databaseService.findRecordByHql(hql), request);
		}
	}
}