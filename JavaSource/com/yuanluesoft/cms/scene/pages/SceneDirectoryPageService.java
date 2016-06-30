package com.yuanluesoft.cms.scene.pages;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.exception.PageNotFoundException;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.scene.pojo.SceneContent;
import com.yuanluesoft.cms.scene.pojo.SceneDirectory;
import com.yuanluesoft.cms.scene.service.SceneService;
import com.yuanluesoft.cms.scene.service.SceneTemplateService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class SceneDirectoryPageService extends PageService {
	private SceneService sceneService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getSitePage(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	protected SitePage getSitePage(String applicationName, String pageName, HttpServletRequest request) throws ServiceException {
		//获取场景目录
		SceneDirectory sceneDirectory = sceneService.getSceneDirectory(Long.parseLong(request.getParameter("id")));
		if(sceneDirectory==null) {
			throw new PageNotFoundException();
		}
		//获取场景服务
		com.yuanluesoft.cms.scene.pojo.SceneService service = sceneService.getSceneServiceByDirectoryId(sceneDirectory.getId());
		sceneDirectory.setSceneService(service);
		
		SitePage sitePage = getPageDefineService().getSitePage(applicationName, ((sceneDirectory instanceof SceneContent) ? "sceneContent" : "scene"));
		//设置record属性
		sitePage.setAttribute("record", sceneDirectory);
		return sitePage;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		SceneTemplateService sceneTemplateService = (SceneTemplateService)getTemplateService();
		return sceneTemplateService.getTemplateHTMLDocument(sitePage.getName(), Long.parseLong(request.getParameter("id")), siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request);
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
