package com.yuanluesoft.jeaf.sso.pages;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.service.UserPageTemplateService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 登录页面服务
 * @author linchuan
 *
 */
public class LoginPageService extends PageService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#writePage(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, java.io.PrintWriter, boolean)
	 */
	public void writePage(String applicationName, String pageName, HttpServletRequest request, HttpServletResponse response, PrintWriter writer, boolean editMode) throws ServiceException, IOException {
		pageName = ("true".equals(request.getParameter("external")) ? "externalLogin" : "internalLogin");
		if("dialog".equals(request.getParameter("displayMode"))) {
			pageName += "Dialog";
		}
		super.writePage(applicationName, pageName, request, response, writer, editMode);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#getTemplate(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument getTemplate(String applicationName, String pageName, SitePage sitePage, long siteId, long themeId, int themeType, int pageWidth, boolean flashSupport, boolean temporaryOpeningFirst, HttpServletRequest request, boolean editMode) throws ServiceException {
		if(sitePage.getName().startsWith("internalLogin")) { //内部用户
			//调用用户页面模板服务获取模板
			return ((UserPageTemplateService)getTemplateService()).getTemplateHTMLDocument(applicationName, pageName, RequestUtils.getSessionInfo(request), request);
		}
		return super.getTemplate(applicationName, pageName, sitePage, siteId, themeId, themeType, pageWidth, flashSupport, temporaryOpeningFirst, request, editMode);
	}
}