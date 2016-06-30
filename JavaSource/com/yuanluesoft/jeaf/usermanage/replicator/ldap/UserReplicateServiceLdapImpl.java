package com.yuanluesoft.jeaf.usermanage.replicator.ldap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * LDAP用户复制
 * @author xujianxiong
 *
 */
public class UserReplicateServiceLdapImpl implements UserReplicateService {
	private String ldapURL = "ldap://localhost:389"; //LDAPURL
	private String ldapUser; //LDAP用户
	private String ldapPassword; //LDAP密码
	private String ldapRoot; //LDAP根
	private boolean ladpRootRegistAsUnit = false; //是否把LDAP根注册成为单位
	
	private OrgService orgService; //组织机构服务
	private PersonService personService; //用户服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#replicate()
	 */
	public void replicate() throws ServiceException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");//声明使用LDAP服务
		env.put(Context.PROVIDER_URL, ldapURL);//访问LDAP必须的参数
		env.put(Context.SECURITY_PRINCIPAL, ldapUser);
		env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
		DirContext ctx = null;
		try {
			Map personNameMap = new HashMap(); //用户名列表
			Map orgIdMap = new HashMap(); //部门列表
			//获取用户列表
			ctx = new InitialDirContext(env);
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			ctls.setReturningAttributes(new String[] {});
			NamingEnumeration searchList = ctx.search("O=" + ldapRoot, "objectClass=person", ctls); //获取用户列表
			while(searchList.hasMoreElements()) {
				SearchResult searchResult = (SearchResult)searchList.next();
				String ladpUserName = searchResult.getNameInNamespace();
				ladpUserName = ladpUserName.replaceAll("CN=", "").replaceAll("OU=", "").replaceAll("O=", "");
				String loginName = ladpUserName.replace(',', '/').toLowerCase();
				personNameMap.put(loginName, ladpUserName);
			}
			//复制用户
			replicatePersons(personNameMap, orgIdMap);
			//删除已经不存在的部门
			deleteDisabledOrgs(ldapRoot, orgIdMap);
			//删除已经不存在的用户
			deleteDisabledPersons(ldapRoot, personNameMap, orgIdMap);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try {
				ctx.close();
			} 
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 复制用户
	 * @param personNameMap
	 * @param orgIdMap
	 * @throws ServiceException
	 */
	private void replicatePersons(Map personNameMap, Map orgIdMap) throws ServiceException {
		for(Iterator iterator = personNameMap.keySet().iterator(); iterator.hasNext();) { 
			String loginName = (String)iterator.next();
			String ladpUserName = (String)personNameMap.get(loginName);
			if(Logger.isDebugEnabled()) {
				Logger.debug("UserReplicateService: replicate person " + loginName + ".");
			}
			int index = ladpUserName.indexOf(",");
			//获取组织机构ID
			long orgId = getOrgId(ladpUserName.substring(index + 1), orgIdMap);
			//按LDAP用户名(也就是loginName)获取用户
			List persons = personService.listPersonsByLoginNames(loginName);
			if(persons!=null && !persons.isEmpty()) { //已经注册过
				if(Logger.isDebugEnabled()) {
					Logger.debug("UserReplicateService: update person " + loginName + " subjection.");
				}
				personService.updatePersonSubjection(((Person)persons.get(0)).getId(), "" + orgId); //更新用户所在组织机构
				continue;
			}
			//没有注册过
			String name = ladpUserName.substring(0, index);
			//检查同名的用户是否已经存在
			persons = personService.listPersonsByName(name);
			if(persons==null || persons.size()!=1 || personNameMap.get(((Person)persons.get(0)).getLoginName())!=null) { //同名用户不存在，或者不只一个，或者还在用户列表中
				if(Logger.isDebugEnabled()) {
					Logger.debug("UserReplicateService: regist new person " + loginName + ".");
				}
				//注册新用户
				personService.addEmployee(UUIDLongGenerator.generateId(), name, loginName, "domino050718", 'M', null, null, null, null, null, "" + orgId, 0, "LDAP");
			}
			else { //同名用户存在，且只一个，且不在用户列表中
				Person person = (Person)persons.get(0);
				person = (Person)personService.load(person.getClass(), person.getId());
				if(Logger.isDebugEnabled()) {
					Logger.debug("UserReplicateService: rename person " + person.getLoginName() + " to " + loginName + ".");
				}
				//更新用户所在组织机构
				personService.updatePersonSubjection(person.getId(), "" + orgId); //更新用户所在组织机构
				//修改用户的登录用户名
				person.setLoginName(loginName);
				personService.update(person);
			}
		}
	}
	
	/**
	 * 获取组织机构ID
	 * @param orgName 格式: 测试组,研发部,hsj_server
	 * @param orgIdMap 已经登记过的组织机构ID
	 * @return
	 * @throws ServiceException
	 */
	private long getOrgId(String orgName, Map orgIdMap) throws ServiceException {
		Long orgId = (Long)orgIdMap.get(orgName);
		if(orgId!=null) {
			return orgId.longValue();
		}
		int index = orgName.indexOf(',');
		long parentOrgId = 0;
		String orgType = null;
		if(index==-1) { //根目录
			orgType = (ladpRootRegistAsUnit ? "unit" : "category");
		}
		else {
			String parentOrgName = orgName.substring(index + 1);
			parentOrgId = getOrgId(parentOrgName, orgIdMap);
			orgType = (parentOrgName.indexOf(',')==-1 && !ladpRootRegistAsUnit ? "unit" : "unitDepartment");
		}
		Org org = (Org)orgService.createDirectory(-1, parentOrgId, (index==-1 ? orgName : orgName.substring(0, index)), orgType, "LDAP", 0, "LDAP");
		orgIdMap.put(orgName, new Long(org.getId()));
		return org.getId();
	}
	
	/**
	 * 删除已经被注销的组织机构
	 * @param ldapRoot
	 * @param orgIdMap
	 * @throws ServiceException
	 */
	private void deleteDisabledOrgs(String ldapRoot, Map orgIdMap) throws ServiceException {
		//获取LDAP根对应的组织机构ID
		long ldapRootOrgId = ((Long)orgIdMap.get(ldapRoot)).longValue();
		//获取子目录列表
		List childOrgs = orgService.listAllChildDirectories(ldapRootOrgId, null, true);
		if(childOrgs==null || childOrgs.isEmpty()) {
			return;
		}
		Collection orgIds = orgIdMap.values();
		for(Iterator iterator = childOrgs.iterator(); iterator.hasNext();) {
			Org org = (Org)iterator.next();
			try {
				if(!orgIds.contains(new Long(org.getId()))) {
					orgService.delete(org);
				}
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 删除已经被注销的用户
	 * @param ldapRoot
	 * @param personNameMap
	 * @param orgIdMap
	 * @throws ServiceException
	 */
	private void deleteDisabledPersons(String ldapRoot, Map personNameMap, Map orgIdMap) throws ServiceException {
		//获取LDAP根对应的组织机构ID
		long ldapRootOrgId = ((Long)orgIdMap.get(ldapRoot)).longValue();
		for(int offset=0; ; offset+=200) {
			//获取用户列表
			List persons = orgService.listOrgPersons("" + ldapRootOrgId, null, true, false, offset, 200);
			if(persons==null || persons.isEmpty()) {
				break;
			}
			for(Iterator iterator = persons.iterator(); iterator.hasNext();) {
				Person person = (Person)iterator.next();
				try {
					if(personNameMap.get(person.getLoginName())==null) {
						personService.delete(person);
						offset--;
					}
				}
				catch(Exception e) {
					
				}
			}
			if(persons.size()<200) {
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#modifyPerson(com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void modifyPerson(Person person) throws ServiceException {
		//不支持
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#deleteOrg(long, long)
	 */
	public void deleteOrg(long orgId, long parentOrgId) throws ServiceException {
		//不支持
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#deletePerson(long)
	 */
	public void deletePerson(long personId) throws ServiceException {
		//不支持
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#modifyOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void modifyOrg(Org org) throws ServiceException {
		//不支持
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#registOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void registOrg(Org org) throws ServiceException {
		//不支持
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.replicator.UserReplicateService#registPerson(com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void registPerson(Person person) throws ServiceException {
		//不支持
	}

	/**
	 * @return the ldapPassword
	 */
	public String getLdapPassword() {
		return ldapPassword;
	}

	/**
	 * @return the ldapRoot
	 */
	public String getLdapRoot() {
		return ldapRoot;
	}

	/**
	 * @return the ldapURL
	 */
	public String getLdapURL() {
		return ldapURL;
	}

	/**
	 * @return the ldapUser
	 */
	public String getLdapUser() {
		return ldapUser;
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @return the ladpRootRegistAsUnit
	 */
	public boolean isLadpRootRegistAsUnit() {
		return ladpRootRegistAsUnit;
	}

	/**
	 * @param ladpRootRegistAsUnit the ladpRootRegistAsUnit to set
	 */
	public void setLadpRootRegistAsUnit(boolean ladpRootRegistAsUnit) {
		this.ladpRootRegistAsUnit = ladpRootRegistAsUnit;
	}

	/**
	 * @param ldapPassword the ldapPassword to set
	 */
	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	/**
	 * @param ldapRoot the ldapRoot to set
	 */
	public void setLdapRoot(String ldapRoot) {
		this.ldapRoot = ldapRoot;
	}

	/**
	 * @param ldapURL the ldapURL to set
	 */
	public void setLdapURL(String ldapURL) {
		this.ldapURL = ldapURL;
	}

	/**
	 * @param ldapUser the ldapUser to set
	 */
	public void setLdapUser(String ldapUser) {
		this.ldapUser = ldapUser;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}