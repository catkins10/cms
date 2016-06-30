package com.yuanluesoft.cms.onlineservice.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceDirectory;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceTemplateService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 网上办事目录页面服务
 * @author linchuan
 *
 */
public class OnlineServiceDirectoryPageService extends PageService {
	private OnlineServiceDirectoryService onlineServiceDirectoryService; //网上办事目录服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取办理事项
		String directoryId = request.getParameter("id");
		OnlineServiceDirectory onlineServiceDirectory;
		if(directoryId!=null) {
			onlineServiceDirectory = (OnlineServiceDirectory)onlineServiceDirectoryService.getDirectory(Long.parseLong(directoryId));
		}
		else {
			onlineServiceDirectory = onlineServiceDirectoryService.getDirectoryBySiteId(siteId);
		}
		if(onlineServiceDirectory==null) {
			throw new PageNotFoundException();
		}
		//设置record属性
		sitePage.setAttribute("record", onlineServiceDirectory);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		OnlineServiceDirectory onlineServiceDirectory = (OnlineServiceDirectory)sitePage.getAttribute("record");
		return ((OnlineServiceTemplateService)getTemplateService()).getTemplateHTMLDocument(pageName, onlineServiceDirectory.getId(), null, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}

	/**
	 * @return the onlineServiceDirectoryService
	 */
	public OnlineServiceDirectoryService getOnlineServiceDirectoryService() {
		return onlineServiceDirectoryService;
	}

	/**
	 * @param onlineServiceDirectoryService the onlineServiceDirectoryService to set
	 */
	public void setOnlineServiceDirectoryService(
			OnlineServiceDirectoryService onlineServiceDirectoryService) {
		this.onlineServiceDirectoryService = onlineServiceDirectoryService;
	}
}