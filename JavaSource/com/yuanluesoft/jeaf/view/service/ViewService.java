/*
 * Created on 2006-3-25
 *
 */
package com.yuanluesoft.jeaf.view.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;

/**
 * 
 * @author linchuan
 * 
 */
public interface ViewService {
	
	/**
	 * 填充视图包
	 * @param viewPackage
	 * @param view
	 * @param beginRow
	 * @param retrieveDataOnly
	 * @param readRecordsOnly
	 * @param countRecordsOnly
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 * @throws PrivilegeException
	 */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException;
	
	/**
	 * 获取指定分类(catrgories)下的子分类,返回Object数组,{分类标题,分类值}
	 * @param view
	 * @param currentCategories 格式:分类1标题|分类1名称,...,分类n标题|分类n名称
	 * @param request
	 * @param sessionInfo
	 * @param max
	 * @return
	 * @throws ServiceException
	 * @throws PrivilegeException
	 */
	public List listChildCategories(View view, final String currentCategories, HttpServletRequest request, final SessionInfo sessionInfo, int limit) throws ServiceException, PrivilegeException;
	
	/**
	 * 获取视图操作列表,过滤没有权限的操作
	 * @param view
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void retrieveViewActions(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
     * 重设视图列
     * @param view
	 * @param viewMode
	 * @param request
	 * @param sessionInfo
	 * @param displayMode
     * @throws ServiceException
     */
    public void resetViewColumns(View view, String viewMode, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 检查视图加载权限
	 * @param view
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean checkLoadPrivilege(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException;
}