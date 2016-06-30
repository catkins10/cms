package com.yuanluesoft.enterprise.exam.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.enterprise.exam.pojo.ExamTest;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;

/**
 * 
 * @author linchuan
 *
 */
public class ExamTestPageService extends PageService {
	private HTMLParser htmlParser; //HTML解析器

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		ExamTest examTest = (ExamTest)request.getAttribute("record");
		if("examTestCheck".equals(sitePage.getName()) && examTest.getStatus()!=ExamService.EXAM_TEST_STATUS_COMPLETE) { //复核、考试未完成
			//删除“完成复核”按钮
			HTMLElement button = (HTMLElement)template.getElementById("button");
			if("完成复核".equals(button.getTitle())) {
				button.getParentNode().removeChild(button);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetFieldElement(org.w3c.dom.html.HTMLAnchorElement, java.lang.String, org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest)
	 */
	public void resetFieldElement(HTMLAnchorElement fieldElement, String fieldName, HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request) throws ServiceException {
		super.resetFieldElement(fieldElement, fieldName, template, applicationName, pageName, sitePage, siteId, request);
		if("timeLeft".equals(fieldName)) { //剩余时间
			ExamTest examTest = (ExamTest)request.getAttribute("record");
			if(examTest.getStatus()!=ExamService.EXAM_TEST_STATUS_TESTING) { //不在考试状态
				fieldElement.getParentNode().removeChild(fieldElement);
			}
			else {
				fieldElement.setId("timeLeft");
				fieldElement.removeAttribute("urn");
				htmlParser.setTextContent(fieldElement, examTest.getExam().getExamTime() + "分钟");
			}
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
}