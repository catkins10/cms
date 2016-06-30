package com.yuanluesoft.bidding.enterprise.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingAgentLib;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 代理名录库页面服务
 * @author linchuan
 *
 */
public class BiddingAgentLibPageService extends PageService {
	private EnterpriseService enterpriseService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		//设置标题
		String title = template.getTitle();
		template.setTitle(request.getParameter("lib") + (title==null || title.equals("") ? "" : " - " + title));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.spring.BasePageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		BiddingAgentLib agentLib = (BiddingAgentLib)ListUtils.findObjectByProperty(enterpriseService.listAgentLibs(), "lib", request.getParameter("lib"));
		if(agentLib==null) {
			throw new PageNotFoundException();
		}
		//设置record属性
		sitePage.setAttribute("record", agentLib);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/**
	 * @return the enterpriseService
	 */
	public EnterpriseService getEnterpriseService() {
		return enterpriseService;
	}

	/**
	 * @param enterpriseService the enterpriseService to set
	 */
	public void setEnterpriseService(EnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
	}
}