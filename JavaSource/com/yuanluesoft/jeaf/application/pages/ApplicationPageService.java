package com.yuanluesoft.jeaf.application.pages;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLIFrameElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.eai.client.EAIClient;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.usermanage.pages.UserPageService;

/**
 * 应用页面服务
 * @author linchuan
 *
 */
public class ApplicationPageService extends UserPageService {
	private EAIClient eaiClient;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		try {
			String applicationTitle = eaiClient.getApplicationTitle(request.getParameter("applicationName"));
			template.setTitle(applicationTitle + (template.getTitle()==null || template.getTitle().equals("") || template.getTitle().equals("应用程序") ? "" : " - " + template.getTitle()));
		}
		catch (Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#buildHTMLDocument(org.w3c.dom.html.HTMLDocument, long, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode) throws ServiceException, IOException {
		HTMLDocument htmlDocument = super.buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode);
		HTMLElement view = (HTMLElement)htmlDocument.getElementById("view");
		if((view instanceof HTMLIFrameElement) && htmlDocument.getElementById("navigator")==null) { //没有导航子页面
			NodeList links = htmlDocument.getElementsByTagName("a");
			for(int i=0; i<(links==null ? 0 : links.getLength()); i++) {
				HTMLAnchorElement a = (HTMLAnchorElement)links.item(i);
				if("view".equals(a.getTarget()) && a.getHref()!=null) {
					HTMLIFrameElement viewFrame = (HTMLIFrameElement)view;
					String src = viewFrame.getSrc();
					String href = a.getHref();
					viewFrame.setSrc(href + (href.indexOf('?')==-1 ? '?' : '&') + src.substring(src.indexOf('?') + 1));
					break;
				}
			}
		}
		return htmlDocument;
	}

	/**
	 * @return the eaiClient
	 */
	public EAIClient getEaiClient() {
		return eaiClient;
	}

	/**
	 * @param eaiClient the eaiClient to set
	 */
	public void setEaiClient(EAIClient eaiClient) {
		this.eaiClient = eaiClient;
	}
}