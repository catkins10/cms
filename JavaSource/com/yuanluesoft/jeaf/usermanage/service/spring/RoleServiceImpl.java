/*
 * Created on 2004-12-30
 *
 */
package com.yuanluesoft.jeaf.usermanage.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class RoleServiceImpl extends BusinessServiceImpl implements RoleService {
	private UserSynchClientList userSynchClientList;
	private SessionService sessionService;
	private PersonService personService; //用户服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
		Role role = (Role)record;
		role.setLastModified(DateTimeUtils.now());
		super.save(record);
		try {
			userSynchClientList.saveRole((Role)record, null, ListUtils.join(role.getMembers(), "memberId", ",", false), ListUtils.join(role.getMembers(), "memberName", ",", false));
		}
		catch (UserSynchException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		saveRoleMember(role, true);
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		String oldName = (String)getDatabaseService().findRecordByHql("select Role.roleName from Role Role where Role.id=" + ((Role)record).getId());
		Role role = (Role)record;
		role.setLastModified(DateTimeUtils.now());
		super.update(record);
		saveRoleMember(role, false);
		try {
			userSynchClientList.saveRole((Role)record, oldName, ListUtils.join(role.getMembers(), "memberId", ",", false), ListUtils.join(role.getMembers(), "memberName", ",", false));
		} 
		catch (UserSynchException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		try {
			sessionService.removeAllSessionInfo();
		}
		catch (SessionException e) {
			Logger.exception(e);
		}
		return record;
	}
	

	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		try {
			userSynchClientList.deleteRole((Role)record);
		} 
		catch (UserSynchException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		super.delete(record);
		try {
			sessionService.removeAllSessionInfo();
		}
		catch (SessionException e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.String, long)
	 */
	public void delete(String pojoClassName, long id) throws ServiceException {
		delete(getDatabaseService().findRecordById(pojoClassName, id));
	}
	
	/**
	 * 保存角色成员
	 * @param memberSet
	 */
	private void saveRoleMember(Role role, boolean isNewRole) {
		if(role.getMembers()==null) {
			return;
		}
		if(!isNewRole) {
			getDatabaseService().deleteRecordsByHql("from RoleMember RoleMember where RoleMember.roleId=" + role.getId());
		}
		for(Iterator iterator=role.getMembers().iterator(); iterator.hasNext();) {
			Record record = (Record)iterator.next();
			getDatabaseService().saveRecord(record);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RoleService#getRole(long)
	 */
	public Role getRole(long roleId) throws ServiceException {
		return (Role)getDatabaseService().findRecordById(Role.class.getName(), roleId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RoleService#isMemberOfRole(long, long)
	 */
	public boolean isMemberOfRole(long personId, long roleId) throws ServiceException {
		String hql = "select RoleMember.id from RoleMember RoleMember where RoleMember.roleId=" + roleId + " and RoleMember.memberId=" + personId;
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RoleService#listRoleMembers(java.lang.String)
	 */
	public List listRoleMembers(String roleIds) throws ServiceException {
		if(roleIds==null || roleIds.isEmpty()) {
			return null;
		}
		String hql = "select Person from Person Person, RoleMember RoleMember" + 
					 " where Person.id=RoleMember.memberId" +
					 " and RoleMember.roleId in (" + JdbcUtils.validateInClauseNumbers(roleIds) + ")" +
					 " order by RoleMember.id";
		return getDatabaseService().findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RoleService#listRoleMembersInOrgs(long, java.lang.String, boolean, boolean)
	 */
	public List listRoleMembersInOrgs(String roleIds, String orgIds, boolean containChildOrg, boolean containParentOrg) throws ServiceException {
		String hql = "select Person" +
					 " from Person Person, PersonSubjection PersonSubjection, RoleMember RoleMember" + (containChildOrg || containParentOrg ? ", OrgSubjection OrgSubjection" : "") + 
					 " where Person.id=PersonSubjection.personId" +
					 " and Person.id=RoleMember.memberId" +
					 " and RoleMember.roleId in (" + JdbcUtils.validateInClauseNumbers(roleIds) + ")" +
					 " and (" + (!containChildOrg ? "PersonSubjection.orgId in (" + JdbcUtils.validateInClauseNumbers(orgIds) + ")" : "(PersonSubjection.orgId=OrgSubjection.directoryId and OrgSubjection.parentDirectoryId in (" + JdbcUtils.validateInClauseNumbers(orgIds) + "))") +
					 (!containParentOrg ? "" : " or (PersonSubjection.orgId=OrgSubjection.parentDirectoryId  and OrgSubjection.directoryId in (" + JdbcUtils.validateInClauseNumbers(orgIds) + "))") +
					 ")" +
					 " order by RoleMember.id";
		return getDatabaseService().findRecordsByHql(hql);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RoleService#listRoles(long, boolean)
	 */
	public List listRoles(long orgId, boolean postOnly, boolean readMembers) throws ServiceException {
		String hql = "from Role Role" +
					 " where Role.orgId=" + orgId +
					 (postOnly ? " and Role.isPost=1" : "") +
					 " order by Role.id";
		List roles =  getDatabaseService().findRecordsByHql(hql, readMembers ? listLazyLoadProperties(Role.class) : null, 0, 0);
		return roles;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RoleService#listRolesByIds(java.lang.String)
	 */
	public List listRolesByIds(String roleIds) throws ServiceException {
		if(roleIds==null || roleIds.isEmpty()) {
			return null;
		}
		List roles =  getDatabaseService().findRecordsByHql("from Role Role where Role.id in (" + JdbcUtils.validateInClauseNumbers(roleIds) + ")", 0, 0);
		return ListUtils.sortByProperty(roles, "id", roleIds);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.RoleService#listRolesOfPerson(java.lang.String, boolean)
	 */
	public List listRolesOfPerson(String personIds, boolean postOnly) throws ServiceException {
		String hql = "select Role" +
					 " from Role Role left join Role.members RoleMember" +
					 " where RoleMember.memberId in (" + JdbcUtils.validateInClauseNumbers(personIds) + ")" +
					 (postOnly ? " and Role.isPost=1" : "");
		return getDatabaseService().findRecordsByHql(hql);
	}

	/**
	 * @return Returns the userSynchClientList.
	 */
	public UserSynchClientList getUserSynchClientList() {
		return userSynchClientList;
	}
	
	/**
	 * @param userSynchClientList The userSynchClientList to set.
	 */
	public void setUserSynchClientList(UserSynchClientList userSynchClientList) {
		this.userSynchClientList = userSynchClientList;
	}
	
	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}
	
	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
