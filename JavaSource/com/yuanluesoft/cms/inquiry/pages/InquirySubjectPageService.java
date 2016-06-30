package com.yuanluesoft.cms.inquiry.pages;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.cms.inquiry.pojo.InquiryVote;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.cms.inquiry.services.InquiryTemplateService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.util.PageUtils;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.HTMLTraversalCallback;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InquirySubjectPageService extends PageService {
	private InquiryService inquiryService; //在线调查服务
	private HTMLParser htmlParser; //HTML解析器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		final InquirySubject inquirySubject = (InquirySubject)sitePage.getAttribute("record");
		HTMLDocument htmlDocument = ((InquiryTemplateService)getTemplateService()).getTemplateHTMLDocument(pageName, RequestUtils.getParameterLongValue(request, "id"), siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
		if(inquirySubject.getEndTime()!=null && inquirySubject.getEndTime().before(DateTimeUtils.now())) { //调查已经结束
			//删除提交按钮
			HTMLElement submitInquiryButton = (HTMLElement)htmlDocument.getElementById("submitInquiryButton"); //与旧系统兼容
			if(submitInquiryButton!=null) {
				submitInquiryButton.getParentNode().removeChild(submitInquiryButton);
			}
		}
		
		//获取调查表单
		HTMLElement form = (HTMLElement)htmlDocument.getElementById("inquiry");
		htmlParser.traversalChildNodes(form.getChildNodes(), true, new HTMLTraversalCallback() {
			public boolean processNode(Node node) {
				HTMLElement element = (HTMLElement)node;
				if(!"button".equals(element.getId())) {
					return false;
				}
				if("提交".equals(element.getTitle())) {
					if(inquirySubject.getEndTime()!=null && inquirySubject.getEndTime().before(DateTimeUtils.now())) { //调查已经结束
						element.getParentNode().removeChild(element);
					}
				}
				else if("结果反馈".equals(element.getTitle())) {
					if(inquirySubject.getFeedbacks()==null || inquirySubject.getFeedbacks().isEmpty()) { //没有结果反馈
						element.getParentNode().removeChild(element);
					}
				}
				return true;
			}
		});
		
		//设置页面有效时间
		if(inquirySubject.getEndTime()!=null) {
			PageUtils.setPageExpiresTime(inquirySubject.getEndTime(), request);
		}
		return htmlDocument;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#buildHTMLDocument(org.w3c.dom.html.HTMLDocument, long, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode) throws ServiceException, IOException {
		HTMLDocument htmlDocument = super.buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode);
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		if(sessionInfo!=null) {
			//获取用户已经选中的选项
			List inquiryVotes = inquiryService.listInquiryVotes(RequestUtils.getParameterLongValue(request, "id"), sessionInfo.getUserId());
			if(inquiryVotes!=null && !inquiryVotes.isEmpty()) {
				for(Iterator iterator = inquiryVotes.iterator(); iterator.hasNext();) {
					InquiryVote inquiryVote = (InquiryVote)iterator.next();
					htmlDocument.getElementById("" + inquiryVote.getOptionId()).setAttribute("checked", "true");
					if(inquiryVote.getSupplement()!=null && !inquiryVote.getSupplement().isEmpty()) {
						htmlDocument.getElementById("supplement_" + inquiryVote.getOptionId()).appendChild(htmlDocument.createTextNode(inquiryVote.getSupplement()));
					}
				}
			}
		}
		return htmlDocument;
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

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}
}