package com.yuanluesoft.cms.infopublic.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.infopublic.pojo.PublicMainDirectory;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 信息公开目录页面服务
 * @author linchuan
 *
 */
public class PublicDirectoryPageService extends PageService {
	private PublicDirectoryService publicDirectoryService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		return getTemplateService().getTemplateHTMLDocument(applicationName, pageName, ((Long)sitePage.getAttribute("siteId")).longValue(), themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		long directoryId = RequestUtils.getParameterLongValue(request, "directoryId"); //url指定的目录ID
		long rootDirectoryId; //根目录ID
		if(directoryId>0) { //目录链接,且没有指定站点
			//查找目录对应的主目录
			PublicMainDirectory mainDirectory = publicDirectoryService.getMainDirectory(directoryId);
			siteId = (siteId==0 ? mainDirectory.getSiteId() : siteId); //把主目录隶属站点设置为当前站点
			rootDirectoryId = mainDirectory==null ? 0 : mainDirectory.getId();
		}
		else {
			//按站点获取主目录
			PublicMainDirectory mainDirectory = publicDirectoryService.getMainDirectoryBySite(siteId);
			rootDirectoryId = mainDirectory.getId();
			directoryId = rootDirectoryId;
		}
		sitePage.setAttribute("siteId", new Long(siteId));
		sitePage.setAttribute("rootDirectoryId", new Long(rootDirectoryId));
		sitePage.setAttribute("directoryId", new Long(directoryId));
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/**
	 * @return the publicDirectoryService
	 */
	public PublicDirectoryService getPublicDirectoryService() {
		return publicDirectoryService;
	}

	/**
	 * @param publicDirectoryService the publicDirectoryService to set
	 */
	public void setPublicDirectoryService(
			PublicDirectoryService publicDirectoryService) {
		this.publicDirectoryService = publicDirectoryService;
	}
}
