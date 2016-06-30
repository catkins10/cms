package com.yuanluesoft.cms.advice.processor;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.advice.pojo.AdviceTopic;
import com.yuanluesoft.cms.advice.service.AdviceService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.processor.SiteRecordListProcessor;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class AdviceTopicsProcessor extends SiteRecordListProcessor {
	private AdviceService adviceService; //民意征集服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		RecordListData recordListData = super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		if(!"processingAdviceTopics".equals(view.getName()) || //不是民意征集主题(征集中)
		   recordListData==null || recordListData.getRecords()==null || recordListData.getRecords().isEmpty()) {
			return recordListData;
		}
		//民意征集主题(征集中),获取最小的征集结束时间
		Date minDate = null;
		for(Iterator iterator = recordListData.getRecords().iterator(); iterator.hasNext();) {
			AdviceTopic topic = (AdviceTopic)iterator.next();
			if(topic.getEndDate()!=null && (minDate==null || minDate.after(topic.getEndDate()))) {
				minDate = topic.getEndDate();
			}
		}
		//更新页面有效时间
		PageUtils.setPageExpiresTime(minDate, request);
		return recordListData;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#createStaticPageRebuildBasisByRecordList(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View, long, org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.jeaf.database.DatabaseService, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasisByRecordList(RecordList recordListModel, View view, long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		super.createStaticPageRebuildBasisByRecordList(recordListModel, view, staticPageId, pageElement, sitePage, webDirectory, parentSite, htmlParser, databaseService, request);
		if("completedAdviceTopics".equals(recordListModel.getRecordListName())) { //民意征集主题(已结束)
			String siteIds = getRelatedSiteIds(view, recordListModel, parentSite);
			//获取最快结束的调查主题的结束时间
			String hql = "select AdviceTopic.endDate" +
						 " from AdviceTopic AdviceTopic" +
						 " where AdviceTopic.issue='1'" +
						 (siteIds==null ? "" : " and AdviceTopic.siteId in (" + siteIds + ")") +
						 " and not AdviceTopic.endDate is null" +
						 " and AdviceTopic.endDate>=DATE(" + DateTimeUtils.formatDate(DateTimeUtils.date(), null) + ")" +
						 " order by AdviceTopic.endDate";
			//设置页面有效时间
			PageUtils.setPageExpiresTime((Date)databaseService.findRecordByHql(hql), request);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordUrl(com.yuanluesoft.jeaf.view.model.View, java.lang.Object, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, boolean, javax.servlet.http.HttpServletRequest)
	 */
	protected String getRecordUrl(View view, Object record, String linkTitle, SitePage sitePage, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if("结果反馈".equals(linkTitle)) {
			AdviceTopic adviceTopic = (AdviceTopic)record;
			try {
				if(adviceTopic.getFeedbacks()==null || adviceTopic.getFeedbacks().isEmpty()) {
					return null;
				}
			}
			catch(Exception e) {
				if(!adviceService.hasFeedback(adviceTopic.getId())) {
					return null;
				}
			}
		}
		else if("建议列表".equals(linkTitle)) {
			AdviceTopic adviceTopic = (AdviceTopic)record;
			try {
				if(ListUtils.findObjectByProperty(adviceTopic.getAdvices(), "publicPass", new Character('1'))==null) {
					return null;
				}
			}
			catch(Exception e) {
				if(!adviceService.hasPublicedAdvice(adviceTopic.getId())) {
					return null;
				}
			}
		}
		return super.getRecordUrl(view, record, linkTitle, sitePage, recordListModel, webDirectory, parentSite, requestInfo, request);
	}

	/**
	 * @return the adviceService
	 */
	public AdviceService getAdviceService() {
		return adviceService;
	}

	/**
	 * @param adviceService the adviceService to set
	 */
	public void setAdviceService(AdviceService adviceService) {
		this.adviceService = adviceService;
	}
}