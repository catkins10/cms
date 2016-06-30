package com.yuanluesoft.cms.rsssubscription.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * RSS订阅页面服务
 * @author linchuan
 *
 */
public class RssSubscribePageService extends PageService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		return ((SiteTemplateService)getTemplateService()).getSiteTemplateHTMLDocument(siteId, pageName, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}
}