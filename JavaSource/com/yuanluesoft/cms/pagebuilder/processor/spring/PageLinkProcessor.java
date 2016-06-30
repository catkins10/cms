package com.yuanluesoft.cms.pagebuilder.processor.spring;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SiteLink;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor;
import com.yuanluesoft.cms.pagebuilder.util.LinkUtils;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 页面专有链接处理器
 * @author linchuan
 *
 */
public class PageLinkProcessor implements PageElementProcessor { 

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, final WebDirectory webDirectory, final WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement a = (HTMLAnchorElement)pageElement;
		//获取链接名称
		String urn = pageElement.getAttribute("urn");
		String linkTitle = StringUtils.getPropertyValue(urn, "linkTitle");
		if(linkTitle==null || linkTitle.isEmpty()) {
			linkTitle = urn;
		}
		//查找链接配置
		SiteLink pageLink = (SiteLink)ListUtils.findObjectByProperty(sitePage.getLinks(), "title", linkTitle);
		if(pageLink==null || pageLink.getUrl()==null || pageLink.getUrl().equals("")) {
			a.getParentNode().removeChild(a);
			return;
		}
		a.removeAttribute("id");
		a.removeAttribute("urn");
		Object record = sitePage.getAttribute("record");
		//输出链接
		long relationSiteId = StringUtils.getPropertyLongValue(urn, "siteId", -1);
		LinkUtils.writeLink(a, pageLink.getUrl(), a.getTarget(), webDirectory.getId(), (relationSiteId==-1 ? parentSite.getId() : relationSiteId), record, false, true, sitePage, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElementAsJs(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#createStaticPageRegenerateBasis(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#listStaticPageIdsForModifiedObject(java.lang.Object, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}
}