package com.yuanluesoft.logistics.mobile.pages;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLScriptElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 手机端页面服务
 * @author linchuan
 *
 */
public class LogisticsMobilePageService extends PageService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#buildHTMLDocument(org.w3c.dom.html.HTMLDocument, long, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode) throws ServiceException, IOException {
		HTMLDocument htmlDocument = getPageBuilder().buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode, false, false, false);
		//清除common.js
		NodeList scripts = htmlDocument.getElementsByTagName("script");
		for(int i=scripts.getLength()-1; i>=0; i--) {
			HTMLScriptElement scriptElement = (HTMLScriptElement)scripts.item(i);
			if(scriptElement.getSrc()!=null && scriptElement.getSrc().indexOf("/jeaf/common/js/common.js")!=-1) {
				scriptElement.getParentNode().removeChild(scriptElement);
			}
		}
		//清除form.css
		NodeList links = htmlDocument.getElementsByTagName("link");
		for(int i=links.getLength()-1; i>=0; i--) {
			HTMLLinkElement linkElement = (HTMLLinkElement)links.item(i);
			if(linkElement.getHref()!=null && linkElement.getHref().indexOf("/cms/css/form.css")!=-1) {
				linkElement.getParentNode().removeChild(linkElement);
				break;
			}
		}
		return htmlDocument;
	}
}