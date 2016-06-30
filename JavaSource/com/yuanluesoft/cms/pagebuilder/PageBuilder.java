/*
 * Created on 2007-7-15
 *
 */
package com.yuanluesoft.cms.pagebuilder;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.model.RequestInfo;


/**
 * 
 * @author linchuan
 * 
 */
public interface PageBuilder {
	
	/**
	 * 创建页面HTMLDocument
	 * @param templateDocument
	 * @param siteId
	 * @param sitePage
	 * @param request
	 * @param editMode
	 * @param appendLogger
	 * @param appendCorrection
	 * @param advertSupport
	 * @return
	 * @throws ServiceException
	 */
	public HTMLDocument buildHTMLDocument(HTMLDocument templateDocument, long siteId, SitePage sitePage, HttpServletRequest request, boolean editMode, boolean appendLogger, boolean appendCorrection, boolean advertSupport) throws ServiceException;

	/**
	 * 输出页面
	 * @param templateDocument
	 * @param response
	 * @throws ServiceException
	 */
	public void outputHTMLDocument(HTMLDocument htmlDocument, PrintWriter writer) throws ServiceException;
	
	/**
	 * 处理页面元素
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param sitePage
	 * @param requestInfo TODO
	 * @param request
	 * @throws ServiceException
	 */
	public void processPageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 以脚本方式输出页面元素
	 * @param sitePage
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void writePageElementAsJs(SitePage sitePage, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}