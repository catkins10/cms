package com.yuanluesoft.cms.pagebuilder;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SiteApplication;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePageElement;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public interface PageDefineService {
	
	/**
	 * 创建页面树
	 * @param currentApplicationName
	 * @param sitePageOnly 是否仅网站页面
	 * @param columnSupportOnly 是否只显示栏目的页面,否仅网站页面时有效
	 * @param internalPageOnly 是否仅内部页面
	 * @param removeRootOnlyPage 是否删除根目录专属页面,仅内部页面时有效
	 * @param advertPutSupportOnly 是否支持广告投放
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Tree createPageTree(String currentApplicationName, boolean sitePageOnly, boolean columnSupportOnly, boolean internalPageOnly, boolean removeRootOnlyPage, boolean advertPutSupportOnly, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 创建视图树
	 * @param currentApplicationName
	 * @param currentPageName
	 * @param rssChannelSupportOnly RSS频道
	 * @param totalSupportOnly 支持统计
	 * @param navigatorSupportOnly 支持导航
	 * @param conatinsEmbedViewOnly 是否要求有内嵌视图
	 * @param siteReferenceSupportOnly 是否支持站点引用
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Tree createViewTree(String currentApplicationName, String currentPageName, boolean rssChannelSupportOnly, boolean totalSupportOnly, boolean navigatorSupportOnly, boolean conatinsEmbedViewOnly, boolean siteReferenceSupportOnly, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 创建链接树
	 * @param currentApplicationName
	 * @param currentPageName
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Tree createLinkTree(String currentApplicationName, String currentPageName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 创建表单树
	 * @param currentApplicationName
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Tree createFormTree(String currentApplicationName, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 获取站点应用配置
	 * @param applicationName
	 * @return
	 * @throws ServiceException
	 */
	public SiteApplication getSiteApplication(String applicationName) throws ServiceException;
	
	/**
	 * 获取站点页面配置
	 * @param applicationName
	 * @param pageName
	 * @return
	 * @throws ServiceException
	 */
	public SitePage getSitePage(String applicationName, String pageName) throws ServiceException;
	
	/**
	 * 获取页面元素配置
	 * @param applicationName
	 * @param pageElementName
	 * @return
	 * @throws ServiceException
	 */
	public SitePageElement getPageElement(String applicationName, String pageElementName) throws ServiceException;
	
	/**
	 * 获取页面元素列表
	 * @return
	 * @throws ServiceException
	 */
	public List listPageElements() throws ServiceException;
	
	/**
	 * 获取记录列表配置
	 * @param applicationName
	 * @param recordListName
	 * @param privateRecordList
	 * @param recordClassName 记录类名称,privateRecordList=true时有效
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public View getRecordList(String applicationName, String recordListName, boolean privateRecordList, String recordClassName, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取页面字段列表
	 * @param applicationName
	 * @param pageName
	 * @return
	 * @throws ServiceException
	 */
	public List listPageFields(String applicationName, String pageName) throws ServiceException;
	
	/**
	 * 获取记录列表的字段列表
	 * @param applicationName
	 * @param recordListName
	 * @param privateRecordList
	 * @param recordClassName 记录类名称,privateRecordList=true时有效
	 * @return
	 * @throws ServiceException
	 */
	public List listRecordListFields(String applicationName, String recordListName, boolean privateRecordList, String recordClassName) throws ServiceException;
	
	/**
	 * 保存页面配置
	 * @param siteApplication
	 * @throws ServiceException
	 */
	public void savePageDefine(SiteApplication siteApplication) throws ServiceException;
	
	/**
	 * 重置模板页面HTML字段
	 * @param pageHtmlField
	 * @param applicationName
	 * @param pageName
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void retrievePageHtmlField(Field pageHtmlField, String applicationName, String pageName, SessionInfo sessionInfo) throws ServiceException;
}