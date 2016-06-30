package com.yuanluesoft.cms.inquiry.processor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
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
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class InquiryRecordListProcessor extends SiteRecordListProcessor {
	private InquiryService inquiryService; //调查服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.sitemanage.processor.SiteRecordListProcessor#isUnrelatedToSites(com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.jeaf.view.model.View)
	 */
	protected boolean isUnrelatedToSites(RecordList recordListModel, View view) {
		if("inquiryResults".equals(view.getName())) { //调查结果
			return true;
		}
		return super.isUnrelatedToSites(recordListModel, view);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		RecordListData recordListData;
		if("inquiryResults".equals(view.getName())) { //调查结果
			List inquirySubjects;
			String inquiryIds = RequestUtils.getParameterStringValue(request, "inquiryIds");
			if(inquiryIds!=null) { //调查ID不为空
				inquirySubjects = inquiryService.retrieveInquiryResults(inquiryIds);
			}
			else { //调查ID为空,按主题ID来统计
				InquirySubject inquirySubject = (InquirySubject)inquiryService.load(InquirySubject.class, RequestUtils.getParameterLongValue(request, "id"));
				inquiryService.retrieveInquiryResults(inquirySubject);
				inquirySubjects = ListUtils.generateList(inquirySubject);
			}
			recordListData = new RecordListData(inquirySubjects, inquirySubjects.size());
		}
		else {
			recordListData = super.readRecordListData(view, recordListModel, searchConditions, beginRow, readRecordsOnly, countRecordsOnly, webDirectory, parentSite, sitePage, request);
		}
		//设置页面有效时间
		if((!view.getName().startsWith("processing")) || //不是调查中的主题
			recordListData==null || recordListData.getRecords()==null || recordListData.getRecords().isEmpty()) {
			return recordListData;
		}
		//获取最小的征集结束时间
		Timestamp minTime = null;
		for(Iterator iterator = recordListData.getRecords().iterator(); iterator.hasNext();) {
			InquirySubject inquirySubject = (InquirySubject)iterator.next();
			if(inquirySubject.getEndTime()!=null && (minTime==null || minTime.after(inquirySubject.getEndTime()))) {
				minTime = inquirySubject.getEndTime();
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
		if("completedInquirySubjects".equals(recordListModel.getRecordListName())) { //已结束的主题
			String siteIds = getRelatedSiteIds(view, recordListModel, parentSite);
			//获取最快结束的调查主题的结束时间
			String hql = "select InquirySubject.endTime" +
						 " from InquirySubject InquirySubject" +
						 " where InquirySubject.isPublic='1'" +
						 (siteIds==null ? "" : " and InquirySubject.siteId in (" + siteIds + ")") +
						 " and not InquirySubject.endTime is null" +
						 " and InquirySubject.endTime>=TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.now(), null) + ")" +
						 " order by InquirySubject.endTime";
			//设置页面有效时间
			PageUtils.setPageExpiresTime((Date)databaseService.findRecordByHql(hql), request);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#getRecordUrl(com.yuanluesoft.jeaf.view.model.View, java.lang.Object, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, boolean, javax.servlet.http.HttpServletRequest)
	 */
	protected String getRecordUrl(View view, Object record, String linkTitle, SitePage sitePage, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		if("结果反馈".equals(linkTitle)) {
			InquirySubject inquirySubject = (InquirySubject)record;
			try {
				if(inquirySubject.getFeedbacks()==null || inquirySubject.getFeedbacks().isEmpty()) {
					return null;
				}
			}
			catch(Exception e) {
				if(!inquiryService.hasFeedback(inquirySubject.getId())) {
					return null;
				}
			}
		}
		return super.getRecordUrl(view, record, linkTitle, sitePage, recordListModel, webDirectory, parentSite, requestInfo, request);
	}

	/**
	 * @return the inquiryService
	 */
	public InquiryService getInquiryService() {
		return inquiryService;
	}

	/**
	 * @param inquiryService the inquiryService to set
	 */
	public void setInquiryService(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}
}