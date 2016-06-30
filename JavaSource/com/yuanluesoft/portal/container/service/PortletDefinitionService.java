package com.yuanluesoft.portal.container.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.portal.container.model.PortletDefinition;
import com.yuanluesoft.portal.container.pojo.PortletEntity;

/**
 * 
 * @author linchuan
 *
 */
public interface PortletDefinitionService extends BusinessService {

	/**
	 * 获取PORTLET定义
	 * @param applicationName
	 * @param portletName
	 * @return
	 * @throws ServiceException
	 */
	public PortletDefinition getPortletDefinition(String applicationName, String portletName) throws ServiceException;
	
	/**
	 * 获取portlet实体
	 * @param entityId
	 * @return
	 * @throws ServiceException
	 */
	public PortletEntity getPortletEntity(long entityId) throws ServiceException;
	
	/**
	 * 创建PORTLET目录树
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public Tree createPortletTree(SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取PORTLET实体列表
	 * @param userId
	 * @param siteId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listPortletEntities(long userId, long siteId, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 导出PORTLET实体列表
	 * @param orgId
	 * @param siteId
	 * @param sessionInfo
	 * @param response
	 * @throws ServiceException
	 */
	public void exportPortletEntitiesAsXML(long orgId, long siteId, SessionInfo sessionInfo, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 导入PORTLET实体
	 * @param orgId
	 * @param siteId
	 * @param xmlFilePath
	 * @throws ServiceException
	 */
	public void importPortletEntitiesFromXML(long orgId, long siteId, String xmlFilePath) throws ServiceException;
}