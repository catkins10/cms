/*
 * Created on 2006-3-13
 *
 */
package com.yuanluesoft.jeaf.usermanage.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;

/**
 * 
 * @author linchuan
 *
 */
public interface RoleService extends BusinessService {
	
	/**
	 * 获取组织机构下的角色列表
	 * @param orgId
	 * @param postOnly 是否仅岗位
	 * @param readMembers 是否获取成员
	 * @return
	 * @throws ServiceException
	 */
	public List listRoles(long orgId, boolean postOnly, boolean readMembers) throws ServiceException;
	
	/**
	 * 获取角色
	 * @param roleId
	 * @return
	 * @throws ServiceException
	 */
	public Role getRole(long roleId) throws ServiceException;
	
	/**
	 * 获得一组角色的名称
	 * @param roleIds
	 * @return
	 * @throws ServiceException
	 */
	public List listRolesByIds(String roleIds) throws ServiceException;
	
	/**
	 * 获得角色人员列表(Person列表)
	 * @param roleIds
	 * @return
	 * @throws ServiceException
	 */
	public List listRoleMembers(String roleIds) throws ServiceException;
	
	/**
	 * 获取属于指定部门的角色成员
	 * @param roleIds
	 * @param departmentIds
	 * @param containChildDepartment
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listRoleMembersInOrgs(String roleIds, String orgIds, boolean containChildOrg, boolean containParentOrg) throws ServiceException;

	/**
	 * 获取用户所属角色列表
	 * @param personIds
	 * @param postOnly 是否仅岗位
	 * @return
	 * @throws ServiceException
	 */
	public List listRolesOfPerson(String personIds, boolean postOnly) throws ServiceException;
	
	/**
	 * 检查用户是否属于指定角色
	 * @param personId
	 * @param roleId
	 * @return
	 * @throws ServiceException
	 */
	public boolean isMemberOfRole(long personId, long roleId) throws ServiceException;
}