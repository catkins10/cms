package com.yuanluesoft.jeaf.usermanage.service;

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
public interface UserPageTemplateService extends TemplateService {
	
	/**
	 * 获取用户页面模板
	 * @param applicationName
	 * @param pageName
	 * @param sessionInfo
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getTemplateHTMLDocument(String applicationName, String pageName, SessionInfo sessionInfo, HttpServletRequest request) throws ServiceException;
}