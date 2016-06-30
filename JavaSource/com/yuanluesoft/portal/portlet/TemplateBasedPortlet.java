package com.yuanluesoft.portal.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.portal.container.pojo.PortletEntity;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateBasedPortlet extends BasePortlet {
	
	/* (non-Javadoc)
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			PortletEntity portletEntity = getPortletEntity();
			//获取模板
			String parameter = request.getParameter("siteId");
			long siteId = parameter==null ? 0 : Long.parseLong(parameter);
			HTMLDocument htmlDocument = getTemplateHTMLDocument(portletEntity, siteId, request, null, null);
			if(htmlDocument==null) {
				response.getWriter().write("Portlet template is not exists.");
				return;
			}
			//生成页面
			pageBuilder.buildHTMLDocument(htmlDocument, siteId, new SitePage(), (HttpServletRequest)request, false, false, false, false);
			//重置HTML文档
			resetHTMLDocument(htmlDocument, request, response);
			response.getWriter().write(htmlParser.getBodyHTML(htmlDocument, "utf-8", false));
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
}