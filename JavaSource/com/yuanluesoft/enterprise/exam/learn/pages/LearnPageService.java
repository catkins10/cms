package com.yuanluesoft.enterprise.exam.learn.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.enterprise.exam.learn.pojo.LearnMission;
import com.yuanluesoft.enterprise.exam.learn.service.LearnService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class LearnPageService extends PageService {
	private HTMLParser htmlParser; //HTML解析器
	private LearnService learnService; //学习服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
		LearnMission learnMission = (LearnMission)sitePage.getAttribute("record");
		//启动学习,返回已学习时间
		learnMission.setLearnedTime(learnService.startupLearned(learnMission.getId(), RequestUtils.getSessionInfo(request)));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//添加脚本,用来更新学习时间
		htmlParser.appendScriptFile(template, Environment.getContextPath() + "/enterprise/exam/learn/js/learn.js");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetFieldElement(org.w3c.dom.html.HTMLAnchorElement, java.lang.String, org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest)
	 */
	public void resetFieldElement(HTMLAnchorElement fieldElement, String fieldName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		super.resetFieldElement(fieldElement, fieldName, template, applicationName, pageName, sitePage, siteId, request);
		if("learnedTime".equals(fieldElement)) { //已学习时间
			fieldElement.setId("learnedTime");
			fieldElement.removeAttribute("urn");
			LearnMission learnMission = (LearnMission)request.getAttribute("record");
			htmlParser.setTextContent(fieldElement, learnMission.getLearnedTime() + "分钟");
		}
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

	/**
	 * @return the learnService
	 */
	public LearnService getLearnService() {
		return learnService;
	}

	/**
	 * @param learnService the learnService to set
	 */
	public void setLearnService(LearnService learnService) {
		this.learnService = learnService;
	}
}