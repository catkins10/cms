package com.yuanluesoft.cms.onlineservice.interactive.accept.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 在线申报登录页面
 * @author linchuan
 *
 */
public class AcceptLoginPageService extends PageService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, long, int, int, boolean, boolean, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		HTMLDocument template = super.getTemplate(applicationName, pageName, sitePage, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request, editMode);
		if(template==null) {
			template = super.getTemplate("jeaf/usermanage", "externalLogin", sitePage, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request, editMode);
		}
		return template;
	}
}