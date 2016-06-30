package com.yuanluesoft.cms.interview.pages;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 访谈直播更新页面
 * @author linchuan
 *
 */
public class InterviewLiveUpdatePageService extends PageService {
	private HTMLParser htmlParser;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//获取发言列表配置
		NodeList nodes = template.getElementsByTagName("a");
		String target = request.getParameter("target");
		HTMLAnchorElement targetRecordList = null;
		for(int i=nodes.getLength()-1; i>=0; i--) {
			HTMLAnchorElement a = (HTMLAnchorElement)nodes.item(i);
			if(target.equals(StringUtils.getPropertyValue(a.getAttribute("urn"), "recordListName"))) {
				targetRecordList = a;
				break;
			}
		}
		
		//清除所有内容
		nodes = template.getChildNodes();
		for(int i=nodes.getLength() - 1; i>=0; i--) {
			template.removeChild(nodes.item(i));
		}
		
		//interviewLiveSpeaks.cloneNode(true);
		//重建body
		HTMLBodyElement body = (HTMLBodyElement)template.createElement("body");
		template.appendChild(body);
		if(targetRecordList!=null) {
			body.appendChild(targetRecordList);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#outputHTMLDocument(org.w3c.dom.html.HTMLDocument, long, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean, java.util.Map)
	 */
	protected void outputHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException, IOException {
		getPageBuilder().buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode, false, false, false);
		try {
			htmlParser.writeHTMLDocument(templateDocument, response.getWriter(), "UTF-8");
		}
		catch (IOException e) {
			throw new ServiceException(e.getMessage());
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
