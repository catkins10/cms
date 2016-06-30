package com.yuanluesoft.cms.onlineservice.processor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 网上办事目录链接处理器
 * @author linchuan
 *
 */
public class OnlineServiceDirectoryLinkProcessor implements PageElementProcessor {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.PageElementProcessor#writePageElement(org.w3c.dom.html.HTMLElement, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.jeaf.htmlparser.HTMLParser, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException {
		HTMLAnchorElement anchorElement = (HTMLAnchorElement)pageElement;
		//解析站点链接
		String urn = pageElement.getAttribute("urn");
		String id = pageElement.getId();
		//删除配置属性
		pageElement.removeAttribute("id");
		pageElement.removeAttribute("urn");

		//解析打开方式
		String url = null;
		String openMode = StringUtils.getPropertyValue(urn, "openMode");
		if("onlineServiceDirectoryLink".equals(id)) { //网上办事目录链接
			long directoryId =  StringUtils.getPropertyLongValue(urn, "directoryId", 0); //解析目录ID
			url = "/cms/onlineservice/directory.shtml?id=" + directoryId;
		}
		else if("onlineServiceAuthorityDirectoryLink".equals(id)) { //职权目录链接
			String itemType = StringUtils.getPropertyValue(urn, "itemType");
			try {
				long authorityDirectoryId = RequestUtils.getParameterLongValue(request, "authorityDirectoryId");
				url = "/cms/onlineservice/authorityDirectory.shtml" +
					  "?authorityItemType=" + URLEncoder.encode(itemType, "utf-8") +
					  (authorityDirectoryId==0 ? "" : "&authorityDirectoryId=" + authorityDirectoryId);
			}
			catch(UnsupportedEncodingException e) {
				
			}
			if(itemType.equals(request.getParameter("authorityItemType"))) {
				String styleClass = anchorElement.getAttribute("class");
				anchorElement.setAttribute("class", styleClass!=null && !styleClass.equals("") ? styleClass + " " + styleClass + "Current" : "current");  //自动修改样式
			}
		}
		//输出链接
		LinkUtils.writeLink(anchorElement, url, openMode, parentSite.getId(), parentSite.getId(), null, false, true, sitePage, request);
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