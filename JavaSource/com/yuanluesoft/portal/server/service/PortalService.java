package com.yuanluesoft.portal.server.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.portal.server.model.Portal;

/**
 * 
 * @author linchuan
 *
 */
public interface PortalService extends BusinessService {
	
	/**
	 * 加载PORTAL
	 * @param applicationName
	 * @param pageName
	 * @param userId 用户ID
	 * @param siteId 站点ID
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Portal loadPortal(String applicationName, String pageName, long userId, long siteId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 选择PORTAL页面
	 * @param applicationName
	 * @param pageName
	 * @param userId
	 * @param siteId
	 * @param pageId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Portal selectPortalPage(String applicationName, String pageName, long userId, long siteId, long pageId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 增加PORTAL页面
	 * @param applicationName
	 * @param pageName
	 * @param userId 用户ID
	 * @param siteId 站点ID
	 * @param title 标题
	 * @param style 样式名称
	 * @param layout 布局,2column_40_60/2column_50_50/2column_60_40/3column_25_25_50/3column_25_50_25/3column_33_33_33/3column_40_30_30/3column_50_25_25/4column_25_25_25_25
	 * @param alwaysDisplayPortletButtons 是否总是显示PORTLET按钮
	 * @param initPortletEntityCategory 初始化时载入的PORTLET分类
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public void addPortalPage(String applicationName, String pageName, long userId, long siteId, String title, String style, String layout, boolean alwaysDisplayPortletButtons, String initPortletEntityCategory, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 编辑页面
	 * @param applicationName
	 * @param pageName
	 * @param userId 用户ID
	 * @param siteId 站点ID
	 * @param pageId 页面ID
	 * @param title 标题
	 * @param style 样式名称
	 * @param layout 布局,2column_40_60/2column_50_50/2column_60_40/3column_25_25_50/3column_25_50_25/3column_33_33_33/3column_40_30_30/3column_50_25_25/4column_25_25_25_25
	 * @param alwaysDisplayPortletButtons 是否总是显示PORTLET按钮
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void editPortalPage(String applicationName, String pageName, long userId, long siteId, long pageId, String title, String style, String layout, boolean alwaysDisplayPortletButtons, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 删除页面
	 * @param applicationName
	 * @param pageName
	 * @param userId 用户ID
	 * @param siteId 站点ID
	 * @param pageId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void deletePortalPage(String applicationName, String pageName, long userId, long siteId, long pageId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 删除PORTAL
	 * @param applicationName
	 * @param pageName
	 * @param userId
	 * @param siteId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void deletePortal(String applicationName, String pageName, long userId, long siteId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 添加PORTLET
	 * @param applicationName
	 * @param pageName
	 * @param userId 用户ID
	 * @param siteId 站点ID
	 * @param pageId 页面ID
	 * @param selectedWsrpProducerIds
	 * @param selectedPortletHandles
	 * @param selectedPortletTitles
	 * @param portletStyle
	 * @param columnIndex
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void addPortlets(String applicationName, String pageName, long userId, long siteId, long pageId, String[] selectedWsrpProducerIds, String[] selectedPortletHandles, String[] selectedPortletTitles, String portletStyle, int columnIndex, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 移动PORTLET
	 * @param applicationName
	 * @param pageName
	 * @param userId 用户ID
	 * @param siteId 站点ID
	 * @param pageId 页面ID
	 * @param portletInstanceId PORTLET实例ID
	 * @param portletColumnIndex 列号
	 * @param portletIndex 序号
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void movePortlet(String applicationName, String pageName, long userId, long siteId, long pageId, long portletInstanceId, int portletColumnIndex, int portletIndex, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 最小化PORTLET
	 * @param applicationName
	 * @param pageName
	 * @param userId
	 * @param siteId
	 * @param pageId
	 * @param portletInstanceId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void minimizePortlet(String applicationName, String pageName, long userId, long siteId, long pageId, long portletInstanceId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 还原PORTLET
	 * @param applicationName
	 * @param pageName
	 * @param userId
	 * @param siteId
	 * @param pageId
	 * @param portletInstanceId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void restorePortlet(String applicationName, String pageName, long userId, long siteId, long pageId, long portletInstanceId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 移除PORTLET
	 * @param applicationName
	 * @param pageName
	 * @param userId
	 * @param siteId
	 * @param pageId
	 * @param portletInstanceId
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void removePortlet(String applicationName, String pageName, long userId, long siteId, long pageId, long portletInstanceId, SessionInfo sessionInfo) throws ServiceException;
}