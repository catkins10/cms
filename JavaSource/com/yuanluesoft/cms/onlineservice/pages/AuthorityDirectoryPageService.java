package com.yuanluesoft.cms.onlineservice.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.onlineservice.service.OnlineServiceTemplateService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class AuthorityDirectoryPageService extends PageService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		if(pageName.equals("authorityDirectory") || pageName.equals("authoritySearch")) {
			//根据事项类型获取模板
			String authorityItemType = request.getParameter("authorityItemType");
			return ((OnlineServiceTemplateService)getTemplateService()).getTemplateHTMLDocument(pageName, 0, "全部".equals(authorityItemType) ? null : authorityItemType, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
		}
		return super.getTemplate(applicationName, pageName, sitePage, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request, editMode);
	}
}