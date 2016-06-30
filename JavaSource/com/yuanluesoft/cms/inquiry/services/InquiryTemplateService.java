package com.yuanluesoft.cms.inquiry.services;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 在线调查模板服务
 * @author linchuan
 *
 */
public interface InquiryTemplateService extends TemplateService {

	/**
	 * 按调查主题获取模板
	 * @param pageName
	 * @param subjectId
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
	public HTMLDocument getTemplateHTMLDocument(String pageName, long subjectId, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request) throws ServiceException;
}