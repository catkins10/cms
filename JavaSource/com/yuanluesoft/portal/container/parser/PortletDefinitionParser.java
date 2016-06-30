package com.yuanluesoft.portal.container.parser;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.portal.container.model.PortalApplication;

/**
 * 
 * @author linchuan
 *
 */
public interface PortletDefinitionParser {

	/**
	 * 解析PORTLET
	 * @param applicationName
	 * @param defineFileName
	 * @return
	 * @throws ServiceException
	 */
	public PortalApplication parsePortlets(String applicationName, String defineFileName) throws ParseException;
}