package com.yuanluesoft.portal.container.service;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.portal.container.pojo.PortletEntityTemplate;

/**
 * 
 * @author linchuan
 *
 */
public interface PortletTemplateService extends TemplateService {
	
	/**
	 * 获取PORTLET模板
	 * @param entityId
	 * @param applicationName
	 * @param pageName
	 * @return
	 * @throws ServiceException
	 */
	public PortletEntityTemplate getPortletEntityTemplate(long entityId, String applicationName, String pageName) throws ServiceException;

	/**
	 * 获取PORTLET预置模板文本
	 * @param portletApplication
	 * @param portletName
	 * @param pageApplication
	 * @param pageName
	 * @return
	 * @throws ServiceException
	 */
	public String getNormalTemplate(String portletApplication, String portletName, String pageApplication, String pageName) throws ServiceException;
	
	/**
	 * 获取PORTLET预置模板
	 * @param portletApplication
	 * @param portletName
	 * @param pageApplication
	 * @param pageName
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument getNormalTemplateDocument(String portletApplication, String portletName, String pageApplication, String pageName) throws ServiceException;
}