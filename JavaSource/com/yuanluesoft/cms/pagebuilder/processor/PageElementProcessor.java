package com.yuanluesoft.cms.pagebuilder.processor;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

import java.sql.Timestamp;

/**
 * 页面元素处理器
 * @author yuanluesoft
 *
 */
public interface PageElementProcessor {
	
	/**
	 * 输出页面元素
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	public void writePageElement(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException;

	/**
	 * 按js方式输出页面元素,返回需要额外添加到“writePageElement.js.shtml”请求参数中的参数
	 * @param pageElement
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param sitePage
	 * @param requestInfo
	 * @param request
	 * @return 
	 * @throws ServiceException
	 */
	public String writePageElementAsJs(HTMLElement pageElement, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, SitePage sitePage, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException;

	/**
	 * 创建静态页面重生成的依据,当数据有变化时,以此为依据判断页面是否需要重新生成
	 * @param staticPageId
	 * @param pageElement
	 * @param sitePage
	 * @param webDirectory
	 * @param parentSite
	 * @param htmlParser
	 * @param databaseService
	 * @param request
	 * @throws ServiceException
	 */
	public void createStaticPageRebuildBasis(long staticPageId, HTMLElement pageElement, SitePage sitePage, WebDirectory webDirectory, WebSite parentSite, HTMLParser htmlParser, DatabaseService databaseService, HttpServletRequest request) throws ServiceException;

	/**
	 * 数据变动后,获取需要更新的静态页面列表
	 * @param object
	 * @param modifyAction
	 * @param baseTime 基准时间,用来判断页面是否已经被重建过
	 * @param databaseService
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException;
}