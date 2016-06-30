package com.yuanluesoft.jeaf.application.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 应用导航服务,应用程序可以自行实现
 * @author linchuan
 *
 */
public interface ApplicationNavigatorService {
	
	/**
	 * 获取应用导航
	 * @param applicationName
	 * @param request
	 * @param sessionInfo
	 * @param childDirectoryTypes,应用导航是树模式时有效
	 * @return
	 * @throws ServiceException
	 */
	public ApplicationNavigator getApplicationNavigator(String applicationName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 应用导航是树模式时,获取子节点列表
	 * @param applicationName
	 * @param parentTreeNodeId
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listApplicationNavigatorTreeNodes(String applicationName, String parentTreeNodeId, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 保存应用导航定义,由ApplicationBuilder调用
	 * @param applicationName
	 * @param navigatorDefinition
	 * @throws ParseException
	 */
	public void saveApplicationNavigatorDefinition(String applicationName, ApplicationNavigatorDefinition navigatorDefinition) throws ServiceException;
}