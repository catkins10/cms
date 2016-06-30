package com.yuanluesoft.cms.scene.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.cms.scene.service.SceneTemplateService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SceneServicePageService extends PageService {
	private SceneService sceneService;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		//获取场景服务
		com.yuanluesoft.cms.scene.pojo.SceneService service = sceneService.getSceneService(RequestUtils.getParameterLongValue(request, "id"));
		if(service==null) {
			throw new PageNotFoundException();
		}
		sitePage.setAttribute("record", service);
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		SceneTemplateService sceneTemplateService = (SceneTemplateService)getTemplateService();
		return sceneTemplateService.getTemplateHTMLDocument(pageName, Long.parseLong(request.getParameter("id")), siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
	}

	/**
	 * @return the sceneService
	 */
	public SceneService getSceneService() {
		return sceneService;
	}

	/**
	 * @param sceneService the sceneService to set
	 */
	public void setSceneService(SceneService sceneService) {
		this.sceneService = sceneService;
	}
}