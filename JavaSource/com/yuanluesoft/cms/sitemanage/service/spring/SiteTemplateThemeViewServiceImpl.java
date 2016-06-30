package com.yuanluesoft.cms.sitemanage.service.spring;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;

/**
 * 模板主题视图服务
 * @author linchuan
 *
 */
public class SiteTemplateThemeViewServiceImpl extends ViewServiceImpl {
	private SiteTemplateThemeService siteTemplateThemeService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return siteTemplateThemeService.listSiteThemes(RequestUtils.getParameterLongValue(request, "pageSiteId"), null, -1, -1);
	}

	/**
	 * @return the siteTemplateThemeService
	 */
	public SiteTemplateThemeService getSiteTemplateThemeService() {
		return siteTemplateThemeService;
	}

	/**
	 * @param siteTemplateThemeService the siteTemplateThemeService to set
	 */
	public void setSiteTemplateThemeService(
			SiteTemplateThemeService siteTemplateThemeService) {
		this.siteTemplateThemeService = siteTemplateThemeService;
	}
}