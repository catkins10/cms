/*
 * Created on 2005-11-23
 *
 */
package com.yuanluesoft.jeaf.security.service;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.formula.service.FormulaSupport;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 访问控制服务
 * @author LinChuan
 * 
 */
public interface AccessControlService extends FormulaSupport {
	public final char VISITOR_TYPE_PERSON = '0'; //访问者类型:个人
	public final char VISITOR_TYPE_DEPARTMENT = '1'; //访问者类型:用户
	public final char VISITOR_TYPE_ROLE = '2'; //访问者类型:角色
	
	public final String ACL_APPLICATION_MANAGER = "application_manager";
	public final String ACL_APPLICATION_VISITOR = "application_visitor";
	public final String ACL_LINK_MANAGER = "link_manager";
	public final String ACL_LINK_VISITOR = "link_visitor";

	/**
	 * 获取当前应用集合中应用的权限控制列表
	 * @param applicationName
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public abstract List getAcl(final String applicationName, final SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取用户对链接的权限
	 * @param linkName
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public abstract List getLinkAcl(final String linkName, final SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 获取有指定权限的人员列表
	 * @param applicationName
	 * @param manageUnit
	 * @return
	 * @throws ServiceException
	 */
	public List listVisitors(final String applicationName, final String manageUnit) throws ServiceException;
}