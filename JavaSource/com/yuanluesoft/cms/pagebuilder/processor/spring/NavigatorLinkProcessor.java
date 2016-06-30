package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.Link;
import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 
 * @author linchuan
 *
 */
public class NavigatorLinkProcessor implements PageElementProcessor {
	private ApplicationNavigatorService applicationNavigatorService; //应用导航服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, final WebDirectory webDirectory, final WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement a = (HTMLAnchorElement)pageElement;
		String applicationName = StringUtils.getPropertyValue(a.getAttribute("urn"), "applicationName");
		String linkTitle = StringUtils.getPropertyValue(a.getAttribute("urn"), "title");
		//获取导航栏
		ApplicationNavigator applicationNavigator = applicationNavigatorService.getApplicationNavigator(applicationName, request, RequestUtils.getSessionInfo(request));
		//获取连接
		Link link = (Link)ListUtils.findObjectByProperty(applicationNavigator.getLinks(), "title", linkTitle);
		if(link==null) {
			a.getParentNode().removeChild(a);
			return;
		}
		a.removeAttribute("id");
		a.removeAttribute("urn");
		//输出链接
		LinkUtils.writeLink(a, link.getHref(), (link.getTarget()==null || link.getTarget().isEmpty() ? a.getTarget() : link.getTarget()), webDirectory.getId(), parentSite.getId(), null, false, true, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageGenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}
	
	/**
	 * @return the applicationNavigatorService
	 */
	public ApplicationNavigatorService getApplicationNavigatorService() {
		return applicationNavigatorService;
	}

	/**
	 * @param applicationNavigatorService the applicationNavigatorService to set
	 */
	public void setApplicationNavigatorService(
			ApplicationNavigatorService applicationNavigatorService) {
		this.applicationNavigatorService = applicationNavigatorService;
	}
}