package com.yuanluesoft.jeaf.usermanage.replicator.soap;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.sso.soap.SsoSessionSoapClient;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgLink;
import com.yuanluesoft.jeaf.usermanage.pojo.RoleMember;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.usermanage.soap.model.Org;
import com.yuanluesoft.jeaf.usermanage.soap.model.Person;
import com.yuanluesoft.jeaf.usermanage.soap.model.Role;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 基于WEB服务的用户复制
 * @author xujianxiong
 *
 */
public class UserReplicateServiceSoapImpl implements UserReplicateService {
	private SoapConnectionPool soapConnectionPool; //SOAP连接池
	private SoapPassport soapPassport; //SOAP许可证
	private String userName; //用户名
	private String password; //MD5加密的密码
	private long rootOrgId; //根目录ID
	private OrgService orgService; //组织机构服务
	private RoleService roleService; //角色服务
	private PersonService personService; //用户服务
	private DatabaseService databaseService; //数据库服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#replicate()
	 */
	public void replicate() throws ServiceException {
		//登录
		String  ssoSessionId = new SsoSessionSoapClient(soapConnectionPool, soapPassport).createSsoSession(userName, password);
		
		//获取根组织,public Org getOrg(long orgId, String ssoSessionId)
		Object[] values = new Object[]{new Long(rootOrgId), ssoSessionId};
		Org org;
		try {
			org = (Org)soapConnectionPool.invokeRemoteMethod("UserService", "getOrg", soapPassport, values, new Class[]{Org.class});
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		
		//更新开始时间
		Timestamp replicateBegin = DateTimeUtils.now();
		
		//复制组织机构
		replicateOrg(org, 0, ssoSessionId, replicateBegin);
		
		//删除已经不存在的部门
		deleteDisabledOrgs(replicateBegin);

		//删除已经不存在的角色
		deleteDisabledRoles(replicateBegin);
		
		//删除已经不存在的用户
		deleteDisabledPersons(replicateBegin);
	}
	
	/**
	 * 递归调用:复制组织机构
	 * @param orgModel
	 * @param parentOrgId
	 * @param ssoSessionId
	 * @throws ServiceException
	 */
	private void replicateOrg(Org orgModel, long parentOrgId, String ssoSessionId, Timestamp replicateBegin) throws ServiceException {
		if(Logger.isTraceEnabled()) {
			Logger.trace("UserReplicateService: replicate org " + orgModel.getName() + ".");
		}
		try {
			//添加或者更新组织机构
			orgService.addOrg(orgModel.getId(), parentOrgId, orgModel.getName(), orgModel.getFullName(), orgModel.getType(), orgModel.getPriority(), orgModel.getLinkedOrgId(), orgModel.getLeaderIds(), orgModel.getLeaderNames(), orgModel.getSupervisorIds(), orgModel.getSupervisorNames(), "自动同步");
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		
		//复制角色列表
		replicateRoles(orgModel.getId(), ssoSessionId);
		
		//复制用户列表
		replicatePersons(orgModel.getId(), ssoSessionId, replicateBegin);
		
		//获取下级目录,public Org[] listChildOrgs(long parentOrgId, String ssoSessionId)
		Org[] childOrgs = null;
		try {
			Object[] values = {new Long(orgModel.getId()), ssoSessionId};
			childOrgs = (Org[])soapConnectionPool.invokeRemoteMethod("UserService", "listChildOrgs", soapPassport, values, new Class[]{Org.class});
		}
		catch(Exception e) {
			
		}
		for(int i=0; i<(childOrgs==null ? 0 : childOrgs.length); i++) {
			replicateOrg(childOrgs[i], orgModel.getId(), ssoSessionId, replicateBegin); //递归调用
		}
	}
	
	/**
	 * 同步组织结构列表
	 * @param orgId
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	private void replicateRoles(long orgId, String ssoSessionId) throws ServiceException {
		//获取角色列表,public Role[] listRoles(long orgId, String ssoSessionId)
		Role[] roles = null;
		try {
			Object[] values = {new Long(orgId), ssoSessionId};
			roles = (Role[])soapConnectionPool.invokeRemoteMethod("UserService", "listRoles", soapPassport, values, new Class[]{Role.class});
		}
		catch(Exception e) {
			
		}
		for(int i=0; i<(roles==null ? 0 : roles.length); i++) {
			if(Logger.isTraceEnabled()) {
				Logger.trace("UserReplicateService: replicate role " + roles[i].getName() + ".");
			}
			try {
				//获取角色
				com.yuanluesoft.jeaf.usermanage.pojo.Role role = roleService.getRole(roles[i].getId());
				boolean isNew = role==null;
				if(isNew) {
					role = new com.yuanluesoft.jeaf.usermanage.pojo.Role();
					role.setId(roles[i].getId());
					role.setOrgId(orgId);
				}
				role.setRoleName(roles[i].getName());
				role.setIsPost(roles[i].isPost() ? 1 : 0);
				role.setRemark("自动同步"); //备注
				//设置成员列表
				Set members = new HashSet();
				if(roles[i].getMemberIds()!=null && !roles[i].getMemberIds().isEmpty()) {
					String[] ids = roles[i].getMemberIds().split(",");
					String[] names = roles[i].getMemberNames().split(",");
					for(int j=0; j<ids.length; j++) {
						RoleMember roleMember = new RoleMember();
						roleMember.setId(UUIDLongGenerator.generateId()); //ID
						roleMember.setMemberId(Long.parseLong(ids[j])); //成员ID
						roleMember.setMemberName(names[j]); //成员姓名
						roleMember.setRoleId(role.getId()); //角色ID
						members.add(roleMember);
					}
				}
				role.setMembers(members);
				if(isNew) {
					roleService.save(role);
				}
				else {
					roleService.update(role);
				}
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 复制用户
	 * @param orgId
	 * @param ssoSessionId
	 * @param replicateBegin
	 * @throws ServiceException
	 */
	private void replicatePersons(long orgId, String ssoSessionId, Timestamp replicateBegin) throws ServiceException {
		//获取用户列表,public Person[] listPersons(long orgId, String ssoSessionId)
		Person[] persons = null;
		try {
			Object[] values = {new Long(orgId), ssoSessionId};
			persons = (Person[])soapConnectionPool.invokeRemoteMethod("UserService", "listPersons", soapPassport, values, new Class[]{Person.class});
		}
		catch(Exception e) {
			
		}
		for(int i=0; i<(persons==null ? 0 : persons.length); i++) {
			if(Logger.isDebugEnabled()) {
				Logger.debug("UserReplicateService: replicate person " + persons[i].getName() + ".");
			}
			try {
				//获取用户
				com.yuanluesoft.jeaf.usermanage.pojo.Person person = personService.getPerson(persons[i].getId());
				String password = persons[i].getPassword()==null || persons[i].getPassword().isEmpty() ? PersonService.PERSON_NOT_LOGIN_PASSWORD : Encoder.getInstance().desBase64Decode(Encoder.getInstance().desBase64Decode(persons[i].getPassword(), "20050718", null), ssoSessionId, null);
				if(person!=null) {
					if(person.getLastModified()!=null && person.getLastModified().after(replicateBegin)) {
						continue;
					}
				}
				else {
					person = personService.addPerson(persons[i].getId(), 
													 persons[i].getType(),
													 persons[i].getName(),
													 persons[i].getLoginName(),
													 password,
													 persons[i].getSex().equals("男") ? 'M' : 'F',
													 persons[i].getTelOffice(),
													 persons[i].getTelFamily(), 
													 persons[i].getCell(), 
													 persons[i].getFamilyAddress(), 
													 persons[i].getMailAddress(), 
													 persons[i].getSubjectionOrgIds(), 
													 0,
													 null);
				}
				//更新用户
				person.setName(persons[i].getName()); //用户名
				person.setSex(persons[i].getSex().equals("男") ? 'M' : 'F'); //性别,男/女
				person.setLoginName(persons[i].getLoginName()); //登录用户名
				person.setPassword(password); //密码
				person.setMailAddress(persons[i].getMailAddress()); //邮箱地址
				person.setFamilyAddress(persons[i].getFamilyAddress()); //家庭地址
				person.setMobile(persons[i].getCell()); //手机
				person.setTel(persons[i].getTelOffice()); //办公室电话
				person.setTelFamily(persons[i].getTelFamily()); //家庭电话
				person.setPriority(persons[i].getPriority()); //优先级,用来做用户排序
				person.setOrgIds(persons[i].getSubjectionOrgIds()); //用户隶属的组织机构ID列表
				person.setSupervisorIds(persons[i].getSupervisorIds()==null ? "" : persons[i].getSupervisorIds()); //分管领导ID列表
				person.setSupervisorNames(persons[i].getSupervisorNames()==null ? "" : persons[i].getSupervisorNames()); //分管领导姓名列表
				person.setRemark("自动同步");
				personService.update(person);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 删除已经被注销的组织机构
	 * @param replicateBegin
	 * @throws ServiceException
	 */
	private void deleteDisabledOrgs(Timestamp replicateBegin) throws ServiceException {
		String hql = "select Org.id" +
					 " from Org Org" +
					 " where Org.remark='自动同步'" +
					 " and Org.id!=0" +
					 " and Org.lastModified<TIMESTAMP(" + DateTimeUtils.formatTimestamp(replicateBegin, null) + ")" +
					 " order by Org.id";
		for(;;) {
			List orgIds = databaseService.findRecordsByHql(hql, 0, 200);
			for(Iterator iterator = orgIds==null ? null : orgIds.iterator(); iterator!=null && iterator.hasNext();) {
				Number orgId = (Number)iterator.next();
				com.yuanluesoft.jeaf.usermanage.pojo.Org org = orgService.getOrg(orgId.longValue());
				if(org!=null) {
					orgService.delete(org);
				}
			}
			if(orgIds==null || orgIds.size()<200) {
				break;
			}
		}
	}
	
	/**
	 * 删除已经被注销的角色
	 * @param replicateBegin
	 * @throws ServiceException
	 */
	private void deleteDisabledRoles(Timestamp replicateBegin) throws ServiceException {
		String hql = "select Role.id" +
					 " from Role Role" +
					 " where Role.remark='自动同步'" +
					 " and Role.lastModified<TIMESTAMP(" + DateTimeUtils.formatTimestamp(replicateBegin, null) + ")" +
					 " order by Role.id";
		for(;;) {
			List roleIds = databaseService.findRecordsByHql(hql, 0, 200);
			for(Iterator iterator = roleIds==null ? null : roleIds.iterator(); iterator!=null && iterator.hasNext();) {
				Number roleId = (Number)iterator.next();
				roleService.delete(roleService.getRole(roleId.longValue()));
			}
			if(roleIds==null || roleIds.size()<200) {
				break;
			}
		}
	}
	
	/**
	 * 删除已经被注销的用户
	 * @param replicateBegin
	 * @throws ServiceException
	 */
	private void deleteDisabledPersons(Timestamp replicateBegin) throws ServiceException {
		String hql = "select Person.id" +
					 " from Person Person" +
					 " where Person.remark='自动同步'" +
					 " and Person.lastModified<TIMESTAMP(" + DateTimeUtils.formatTimestamp(replicateBegin, null) + ")" +
					 " order by Person.id";
		for(;;) {
			List personIds = databaseService.findRecordsByHql(hql, 0, 200);
			for(Iterator iterator = personIds==null ? null : personIds.iterator(); iterator!=null && iterator.hasNext();) {
				Number personId = (Number)iterator.next();
				personService.delete(personService.getPerson(personId.longValue()));
			}
			if(personIds==null || personIds.size()<200) {
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#registPerson(com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void registPerson(com.yuanluesoft.jeaf.usermanage.pojo.Person person) throws ServiceException {
		try {
			//public void registPerson(long personId, String orgIds, String loginName, String name, String password, String sex, String mailAddress, String familyAddress, String cell, String telOffice, String telFamily, String ssoSessionId) throws ServiceException, SoapException;
			String  ssoSessionId = new SsoSessionSoapClient(soapConnectionPool, soapPassport).createSsoSession(userName, password); //登录
			Object[] values = {new Long(person.getId()), person.getOrgIds(), person.getLoginName(), person.getName(), Encoder.getInstance().desBase64Encode(personService.decryptPassword(person.getId(), person.getPassword()), ssoSessionId, null), "" + person.getSex(), person.getMailAddress(), person.getFamilyAddress(), person.getMobile(), person.getTel(), person.getTelFamily(), ssoSessionId};
			soapConnectionPool.invokeRemoteMethod("UserService", "registPerson", soapPassport, values, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#modifyPerson(com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void modifyPerson(com.yuanluesoft.jeaf.usermanage.pojo.Person person) throws ServiceException {
		try {
			//public void modifyPerson(long personId, String orgIds, String loginName, String name, String password, String sex, String mailAddress, String familyAddress, String cell, String telOffice, String telFamily, String ssoSessionId) throws ServiceException, SoapException;
			String  ssoSessionId = new SsoSessionSoapClient(soapConnectionPool, soapPassport).createSsoSession(userName, password); //登录
			Object[] values = {new Long(person.getId()), person.getOrgIds(), person.getLoginName(), person.getName(), Encoder.getInstance().desBase64Encode(personService.decryptPassword(person.getId(), person.getPassword()), ssoSessionId, null), "" + person.getSex(), person.getMailAddress(), person.getFamilyAddress(), person.getMobile(), person.getTel(), person.getTelFamily(), ssoSessionId};
			soapConnectionPool.invokeRemoteMethod("UserService", "modifyPerson", soapPassport, values, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#deletePerson(long)
	 */
	public void deletePerson(long personId) throws ServiceException {
		try {
			//public void deletePerson(long personId, String ssoSessionId) throws ServiceException, SoapException;
			String  ssoSessionId = new SsoSessionSoapClient(soapConnectionPool, soapPassport).createSsoSession(userName, password); //登录
			Object[] values = {new Long(personId), ssoSessionId};
			soapConnectionPool.invokeRemoteMethod("UserService", "deletePerson", soapPassport, values, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#registOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void registOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org org) throws ServiceException {
		try {
			//public void registOrg(long orgId, long parentOrgId, String name, String fullName, String type, float priority, long linkedOrgId, String leaderIds, String leaderNames, String supervisorIds, String supervisorNames, String remark, String ssoSessionId) throws ServiceException, SoapException;
			String  ssoSessionId = new SsoSessionSoapClient(soapConnectionPool, soapPassport).createSsoSession(userName, password); //登录
			Object[] values = {new Long(org.getId()), new Long(org.getParentDirectoryId()), org.getDirectoryName(), (org instanceof Unit ? ((Unit)org).getFullName() : null), org.getDirectoryType(), new Float(org.getPriority()), new Long(org instanceof OrgLink ? ((OrgLink)org).getLinkedDirectoryId() : 0), org.getLeaderIds(), org.getLeaderNames(), org.getSupervisorIds(), org.getSupervisorNames(), org.getRemark(), ssoSessionId};
			soapConnectionPool.invokeRemoteMethod("UserService", "registOrg", soapPassport, values, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#modifyOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void modifyOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org org) throws ServiceException {
		try {
			//public void modifyOrg(long orgId, long parentOrgId, String name, String fullName, String type, float priority, long linkedOrgId, String leaderIds, String leaderNames, String supervisorIds, String supervisorNames, String remark, String ssoSessionId) throws ServiceException, SoapException;
			String  ssoSessionId = new SsoSessionSoapClient(soapConnectionPool, soapPassport).createSsoSession(userName, password); //登录
			Object[] values = {new Long(org.getId()), new Long(org.getParentDirectoryId()), org.getDirectoryName(), (org instanceof Unit ? ((Unit)org).getFullName() : null), org.getDirectoryType(), new Float(org.getPriority()), new Long(org instanceof OrgLink ? ((OrgLink)org).getLinkedDirectoryId() : 0), org.getLeaderIds(), org.getLeaderNames(), org.getSupervisorIds(), org.getSupervisorNames(), org.getRemark(), ssoSessionId};
			soapConnectionPool.invokeRemoteMethod("UserService", "modifyOrg", soapPassport, values, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#deleteOrg(long, long)
	 */
	public void deleteOrg(long orgId, long parentOrgId) throws ServiceException {
		try {
			//deleteOrg(long orgId, long parentOrgId, String ssoSessionId) throws ServiceException;
			String  ssoSessionId = new SsoSessionSoapClient(soapConnectionPool, soapPassport).createSsoSession(userName, password); //登录
			Object[] values = {new Long(orgId), new Long(parentOrgId), ssoSessionId};
			soapConnectionPool.invokeRemoteMethod("UserService", "deleteOrg", soapPassport, values, null);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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

	/**
	 * @return the roleService
	 */
	public RoleService getRoleService() {
		return roleService;
	}

	/**
	 * @param roleService the roleService to set
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * @return the rootOrgId
	 */
	public long getRootOrgId() {
		return rootOrgId;
	}

	/**
	 * @param rootOrgId the rootOrgId to set
	 */
	public void setRootOrgId(long rootOrgId) {
		this.rootOrgId = rootOrgId;
	}

	/**
	 * @return the soapConnectionPool
	 */
	public SoapConnectionPool getSoapConnectionPool() {
		return soapConnectionPool;
	}

	/**
	 * @param soapConnectionPool the soapConnectionPool to set
	 */
	public void setSoapConnectionPool(SoapConnectionPool soapConnectionPool) {
		this.soapConnectionPool = soapConnectionPool;
	}

	/**
	 * @return the soapPassport
	 */
	public SoapPassport getSoapPassport() {
		return soapPassport;
	}

	/**
	 * @param soapPassport the soapPassport to set
	 */
	public void setSoapPassport(SoapPassport soapPassport) {
		this.soapPassport = soapPassport;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}