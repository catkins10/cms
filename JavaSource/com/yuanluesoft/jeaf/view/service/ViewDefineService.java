/*
 * Created on 2005-10-28
 *
 */
package com.yuanluesoft.jeaf.view.service;

import java.util.List;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public interface ViewDefineService {
	
	/**
	 * 获取视图定义,如果sessionInfo不为空,检查用户的访问权限
	 * @param applicationName
	 * @param viewName
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public View getView(String applicationName, String viewName, SessionInfo sessionInfo) throws ServiceException, PrivilegeException;
	
	/**
	 * 获取应用的视图列表,如果sessionInfo不为空,检查用户的访问权限
	 * @param applicationName
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public List listViews(String applicationName, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 保存视图定义
	 * @param applicationName
	 * @param views
	 * @throws ServiceException
	 */
	public void saveViewDefine(String applicationName, List views) throws ServiceException;
}