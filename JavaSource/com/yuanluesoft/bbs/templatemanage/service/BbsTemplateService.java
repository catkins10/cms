package com.yuanluesoft.bbs.templatemanage.service;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface BbsTemplateService extends TemplateService {
	
	/**
	 * 获取模板
	 * @param pageName
	 * @param directoryId 论坛目录ID
	 * @param siteId 站点ID
	 * @param themeId
	 * @param themeType
	 * @param pageWidth
	 * @param flashSupport
	 * @param temporaryOpeningFirst
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getTemplateHTMLDocument(String pageName, long directoryId, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException;
}