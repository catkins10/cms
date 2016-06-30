package com.yuanluesoft.jeaf.usermanage.service.spring;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.spring.SiteFormServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.usermanage.service.UserPageTemplateService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class UserFormServiceImpl extends SiteFormServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.SiteFormServiceImpl#getTemplateDocument(java.lang.String, java.lang.String, long, com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest)
	 */
	protected HTMLDocument getTemplateDocument(String applicationName, String pageName, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, ActionForm actionForm, HttpServletRequest request) throws ServiceException {
		UserPageTemplateService userPageTemplateService = (UserPageTemplateService)getTemplateService();
		return userPageTemplateService.getTemplateHTMLDocument(applicationName, pageName, RequestUtils.getSessionInfo(request), request);
	}
}