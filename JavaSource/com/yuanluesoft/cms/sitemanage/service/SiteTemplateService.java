package com.yuanluesoft.cms.sitemanage.service;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public interface SiteTemplateService extends TemplateService {
	
	/**
	 * 获取站点/栏目模板
	 * @param siteId
	 * @param pageName
	 * @param themeId
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getSiteTemplateHTMLDocument(long siteId, String pageName, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 复制整个站点/栏目的模板
	 * @param toSiteId
	 * @param fromSiteId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void copySiteTemplate(long toSiteId, long fromSiteId, SessionInfo sessionInfo) throws ServiceException;
}