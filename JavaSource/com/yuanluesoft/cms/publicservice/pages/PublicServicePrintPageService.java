package com.yuanluesoft.cms.publicservice.pages;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLInputElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 领导信箱打印页面服务
 * @author linchuan
 *
 */
public class PublicServicePrintPageService extends PageService {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#setPageAttributes(java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void setPageAttributes(String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, boolean editMode) throws ServiceException {
		super.setPageAttributes(applicationName, pageName, sitePage, siteId, request, editMode);
		sitePage.setAttribute("fullAccess", "true"); //设置为完全访问,以显示意见填写人
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#resetTemplate(org.w3c.dom.html.HTMLDocument, java.lang.String, java.lang.String, com.yuanluesoft.cms.sitemanage.model.SitePage, long, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected void resetTemplate(HTMLDocument template, String applicationName, String pageName, SitePage sitePage, long siteId, HttpServletRequest request, HttpServletResponse response, boolean editMode) throws ServiceException {
		super.resetTemplate(template, applicationName, pageName, sitePage, siteId, request, response, editMode);
		template.getBody().setAttribute("onload", "window.print()");
		template.getBody().setAttribute("contentEditable", "true");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.PageService#buildHTMLDocument(org.w3c.dom.html.HTMLDocument, long, com.yuanluesoft.cms.sitemanage.model.SitePage, javax.servlet.http.HttpServletRequest, boolean)
	 */
	protected HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode) throws ServiceException, IOException {
		templateDocument = super.buildHTMLDocument(templateDocument, siteId, sitePage, request, editMode);
		//删除所有的隐藏字段
		NodeList inputs = templateDocument.getElementsByTagName("input");
		for(int i=(inputs==null ? -1 : inputs.getLength()-1); i>=0; i--) {
			HTMLInputElement input = (HTMLInputElement)inputs.item(i);
			if(input.getType()!=null && "hidden".equals(input.getType().toLowerCase())) {
				input.getParentNode().removeChild(input);
			}
		}
		return templateDocument;
	}
}