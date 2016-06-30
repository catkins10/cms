package com.yuanluesoft.cms.interview.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;

import com.yuanluesoft.cms.interview.pojo.InterviewSubject;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 访谈直播页面
 * @author linchuan
 *
 */
public class InterviewLivePageService extends PageService {
	private InterviewService interviewService; //在线访谈服务
	private HTMLParser htmlParser;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//创建隐藏字段,存放ID
		HTMLInputElement hidden = (HTMLInputElement)template.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setName("subjectId");
		hidden.setValue(request.getParameter("id"));
		template.getBody().appendChild(hidden);
		//创建隐藏字段,存放页面名称
		hidden = (HTMLInputElement)template.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setName("interviewPageName");
		hidden.setValue(pageName);
		template.getBody().appendChild(hidden);
		//创建隐藏字段,存放站点ID
		hidden = (HTMLInputElement)template.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setName("siteId");
		hidden.setValue("" + siteId);
		template.getBody().appendChild(hidden);
		//插入脚本
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/jeaf/common/js/common.js");
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/cms/interview/js/interview.js");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取访谈主题
		InterviewSubject interviewSubject = (InterviewSubject)request.getAttribute("interviewSubject");
		if(interviewSubject==null) {
			interviewSubject = interviewService.getInterviewSubject(Long.parseLong(request.getParameter("id")));
			if(interviewSubject==null) {
				throw new PageNotFoundException();
			}
		}
		//设置record属性
		sitePage.setAttribute("record", interviewSubject);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/**
	 * @return the interviewService
	 */
	public InterviewService getInterviewService() {
		return interviewService;
	}

	/**
	 * @param interviewService the interviewService to set
	 */
	public void setInterviewService(InterviewService interviewService) {
		this.interviewService = interviewService;
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