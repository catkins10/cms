package com.yuanluesoft.cms.onlineservice.service;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface OnlineServiceTemplateService extends TemplateService {
	
	/**
	 * 按目录和时限类型获取模板
	 * @param pageName
	 * @param directoryId
	 * @param itemType
	 * @param siteId
	 * @param themeId
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getTemplateHTMLDocument(String pageName, long directoryId, String itemType, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException;
}