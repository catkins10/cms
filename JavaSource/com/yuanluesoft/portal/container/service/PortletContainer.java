package com.yuanluesoft.portal.container.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import oasis.names.tc.wsrp.v1.types.PortletDescription;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.portal.container.exception.PortletContainerException;
import com.yuanluesoft.portal.container.internal.MarkupResponse;
import com.yuanluesoft.portal.container.internal.PortletWindow;

/**
 * 
 * @author linchuan
 *
 */
public interface PortletContainer {

    /**
     * Calls the render method of the given portlet window.
     * @param portletWindow  the portlet Window
     * @param request               the servlet request
     * @throws PortletException          if one portlet has trouble fulfilling
     *                                   the request
     * @throws IOException               if the streaming causes an I/O problem
     * @throws PortletContainerException if the portlet container implementation
     *                                   has trouble fulfilling the request
     */
    public MarkupResponse doRender(PortletWindow portletWindow, HttpServletRequest request) throws PortletException, IOException, PortletContainerException, ServiceException;

    /**
     * Indicates that a portlet action occured in the current request and calls
     * the processAction method of this portlet.
     * @param portletWindow the portlet Window
     * @param request               the servlet request
     * @throws PortletException          if one portlet has trouble fulfilling
     *                                   the request
     * @throws PortletContainerException if the portlet container implementation
     *                                   has trouble fulfilling the request
     */
    public MarkupResponse doAction(PortletWindow portletWindow, HttpServletRequest request) throws PortletException, IOException, PortletContainerException, ServiceException;

    /**
     * 保存PORTLET个性设置,由PortletPreferencesImpl调用
     * @param portletWindow
     * @param request
     * @param portletPreferences
     * @throws PortletException
     * @throws IOException
     * @throws PortletContainerException
     */
    public void storePortletPreferences(PortletWindow portletWindow, PortletRequest request, PortletPreferences portletPreferences) throws PortletException, IOException, PortletContainerException, ServiceException;
    
    /**
     * 获取用户PORTLET个性设置(com.yuanluesoft.portal.container.internal.PortletPreference)列表
     * @param portletWindow
     * @param request
     * @throws PortletException
     * @throws IOException
     * @throws PortletContainerException
     */
    public Map getPortletPreferences(PortletWindow portletWindow, PortletRequest request) throws PortletException, IOException, PortletContainerException, ServiceException;
    
    /**
     * 获取默认的PORTLET个性设置(com.yuanluesoft.portal.container.internal.PortletPreference)列表,按用户所在部门或者站点来获取
     * @param portletWindow
     * @param request
     * @throws PortletException
     * @throws IOException
     * @throws PortletContainerException
     */
    public Map getDefaultPortletPreferences(PortletWindow portletWindow, PortletRequest request) throws PortletException, IOException, PortletContainerException, ServiceException;
    
    /**
     * 按portlet实例删除个性设置,避免存放过多的记录
     * @param portletInstanceId
     * @param portletEntityId
     * @throws ServiceException
     */
    public void deletePortletPreferencesByEntity(long portletEntityId) throws ServiceException;
    
    /**
     * 按portlet实例删除个性设置,避免存放过多的记录
     * @param portletInstanceId
     * @param userId
     * @param siteId
     * @throws ServiceException
     */
    public void deletePortletPreferencesByInstance(long portletInstanceId, long userId, long siteId) throws ServiceException;
    
    /**
     * 获取PORTLET实体分组(PortletGroup)列表
     * @param userId
     * @param siteId
     * @param sessionInfo
     * @return
     * @throws ServiceException
     */
    public List listPortletGroups(long userId, long siteId, SessionInfo sessionInfo) throws ServiceException;
    
    /**
     * 清理缓存的portlet
     * @param portletEntityId
     * @throws ServiceException
     */
    public void clearCachedPortlet(long portletEntityId) throws ServiceException;
    
	/**
	 * 获取PORLET描述
	 * @param wsrpProducerId
	 * @param portletHandle
	 * @return
	 * @throws ServiceException
	 */
	public PortletDescription getPortletDescription(String wsrpProducerId, String portletHandle) throws ServiceException;
}