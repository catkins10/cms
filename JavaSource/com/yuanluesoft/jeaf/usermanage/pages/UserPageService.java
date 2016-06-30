package com.yuanluesoft.jeaf.usermanage.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.service.UserPageTemplateService;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.portal.server.pages.PortalPageService;

/**
 * 用户页面服务
 * @author linchuan
 *
 */
public class UserPageService extends PortalPageService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		return ((UserPageTemplateService)getTemplateService()).getTemplateHTMLDocument(applicationName, pageName, RequestUtils.getSessionInfo(request), request);
	}
}