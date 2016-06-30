/*
 * Created on 2007-7-15
 *
 */
package com.yuanluesoft.cms.pagebuilder;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 需要站点支持的
 * @author LinChuan
 *
 */
public interface SiteFormService {
	
	/**
	 * 生成应用表单
	 * @param applicationName
	 * @param pageName
	 * @param siteId
	 * @param templateId
	 * @param actionForm
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public String generateApplicationForm(String applicationName, String pageName, long siteId, long templateId, ActionForm actionForm, HttpServletRequest request) throws ServiceException;
}