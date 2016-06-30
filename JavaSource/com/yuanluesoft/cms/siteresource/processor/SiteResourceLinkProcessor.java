package com.yuanluesoft.cms.siteresource.processor;

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
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 页面元素处理器：资源链接
 * @author linchuan
 *
 */
public class SiteResourceLinkProcessor implements PageElementProcessor { 

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, java.lang.String, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.sitemanage.pojo.Site, com.yuanluesoft.cms.htmlparser.HTMLParser, com.yuanluesoft.cms.templatemanage.parser.TemplateElementParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement;
		//解析资源链接
		String urn = pageElement.getAttribute("urn");
		//解析资源ID
		long resourceId = StringUtils.getPropertyLongValue(urn, "resourceId", -1);
		//解析打开方式
		String openMode = StringUtils.getPropertyValue(urn, "openMode");
		
		//删除配置属性
		pageElement.removeAttribute("id");
		pageElement.removeAttribute("urn");
		
		//输出链接
		LinkUtils.writeLink(anchorElement, "/cms/siteresource/article.shtml?id=" + resourceId, openMode, webDirectory.getId(), parentSite.getId(), null, false, true, sitePage, request);
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