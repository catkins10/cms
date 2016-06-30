/*
 * Created on 2006-7-5
 *
 */
package com.yuanluesoft.jeaf.usermanage.soap;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.SoapException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.soap.BaseSoapService;
import com.yuanluesoft.jeaf.usermanage.member.service.MemberService;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgLink;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSubjection;
import com.yuanluesoft.jeaf.usermanage.pojo.Unit;
import com.yuanluesoft.jeaf.usermanage.service.AgentService;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.service.RoleService;
import com.yuanluesoft.jeaf.usermanage.soap.model.Org;
import com.yuanluesoft.jeaf.usermanage.soap.model.Person;
import com.yuanluesoft.jeaf.usermanage.soap.model.RegisteredUser;
import com.yuanluesoft.jeaf.usermanage.soap.model.Role;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class UserSoapService extends BaseSoapService {
	
	/**
	 * 获取组织机构
	 * @param orgId
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 */
	public Org getOrg(long orgId, String ssoSessionId) throws ServiceException, SoapException {
		OrgService orgService = (OrgService)getSpringService("orgService");
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		//或用户对目录的权限
		List popedoms = orgService.listDirectoryPopedoms(orgId, sessionInfo);
		if(popedoms==null) {
			return null;
		}
		com.yuanluesoft.jeaf.usermanage.pojo.Org org = orgService.getOrg(orgId);
		return org==null ? null : generateOrgModel(org);
	}
	
	/**
	 * 按名称获取组织机构
	 * @param name
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public Org getOrgByName(String name, String ssoSessionId) throws ServiceException, SoapException {
		OrgService orgService = (OrgService)getSpringService("orgService");
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		com.yuanluesoft.jeaf.usermanage.pojo.Org org = (com.yuanluesoft.jeaf.usermanage.pojo.Org)orgService.getDirectoryByName(0, name, false);
		//或用户对目录的权限
		List popedoms = orgService.listDirectoryPopedoms(org.getId(), sessionInfo);
		if(popedoms==null) {
			return null;
		}
		return org==null ? null : generateOrgModel(org);
	}
	
	/**
	 * 生成组织机构模型
	 * @param org
	 * @return
	 */
	private Org generateOrgModel(com.yuanluesoft.jeaf.usermanage.pojo.Org org) {
		Org orgModel = new Org();
		orgModel.setId(org.getId()); //ID
		orgModel.setName(org.getDirectoryName()); //名称
		orgModel.setFullName(org instanceof Unit ? ((Unit)org).getFullName() : null); //全称
		orgModel.setParentOrgId(org.getParentDirectoryId()); //上级目录ID
		orgModel.setPriority(org.getPriority()); //优先级
		orgModel.setType(org.getDirectoryType()); //类型
		orgModel.setLinkedOrgId(org instanceof OrgLink ? ((OrgLink)org).getLinkedDirectoryId() : 0); //引用的组织机构ID
		orgModel.setLeaderIds(ListUtils.join(org.getLeaders(), "leaderId", ",", false)); //部门领导ID列表,用逗号分隔
		orgModel.setLeaderNames(ListUtils.join(org.getLeaders(), "leader", ",", false)); //部门领导姓名列表,用逗号分隔
		orgModel.setSupervisorIds(ListUtils.join(org.getSupervisors(), "supervisorId", ",", false)); //主管领导ID列表,用逗号分隔
		orgModel.setSupervisorNames(ListUtils.join(org.getSupervisors(), "supervisor", ",", false)); //主管领导姓名列表,用逗号分隔
		return orgModel;
	}
	
	/**
	 * 获取下级组织机构列表
	 * @param parentOrgId
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 */
	public Org[] listChildOrgs(long parentOrgId, String ssoSessionId) throws ServiceException, SoapException {
		OrgService orgService = (OrgService)getSpringService("orgService");
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		List childOrgs = orgService.listChildDirectories(parentOrgId, null, "all", null, true, true, sessionInfo, 0, 0);
		if(childOrgs==null || childOrgs.isEmpty()) {
			return null;
		}
		Org[] orgs = new Org[childOrgs.size()];
		for(int i=0; i<orgs.length; i++) {
			orgs[i] = generateOrgModel((com.yuanluesoft.jeaf.usermanage.pojo.Org)childOrgs.get(i));
		}
		return orgs;
	}
	
	/**
	 * 获取角色岗位列表
	 * @param orgId
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 */
	public Role[] listRoles(long orgId, String ssoSessionId) throws ServiceException, SoapException {
		OrgService orgService = (OrgService)getSpringService("orgService");
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		//或用户对目录的权限
		List popedoms = orgService.listDirectoryPopedoms(orgId, sessionInfo);
		if(popedoms==null) {
			return null;
		}
		RoleService roleService = (RoleService)getSpringService("roleService");
		List orgRoles = roleService.listRoles(orgId, false, true);
		if(orgRoles==null || orgRoles.isEmpty()) {
			return null;
		}
		Role[] roles = new Role[orgRoles.size()];
		for(int i=0; i<roles.length; i++) {
			com.yuanluesoft.jeaf.usermanage.pojo.Role role = (com.yuanluesoft.jeaf.usermanage.pojo.Role)orgRoles.get(i);
			roles[i] = new Role();
			roles[i].setId(role.getId()); //ID
			roles[i].setOrgId(orgId); //所在组织机构ID
			roles[i].setName(role.getRoleName()); //名称
			roles[i].setPost(role.getIsPost()==1); //是否岗位
			roles[i].setMemberIds(ListUtils.join(role.getMembers(), "memberId", ",", false)); //成员ID列表,用逗号分隔
			roles[i].setMemberNames(ListUtils.join(role.getMembers(), "memberName", ",", false)); //成员姓名列表,用逗号分隔
		}
		return roles;
	}
	
	/**
	 * 获取用户详细信息
	 * @param personId
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public Person getPerson(long personId, String ssoSessionId) throws ServiceException, SoapException {
		PersonService personService = (PersonService)getSpringService("personService");
		com.yuanluesoft.jeaf.usermanage.pojo.Person person = (com.yuanluesoft.jeaf.usermanage.pojo.Person)personService.load(Person.class, personId);
		if(person==null) {
			return null;
		}
		//检查用户对其所在组织机构的权限
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		int accessLevel = checkPersonPopedom(person, sessionInfo);
		return accessLevel==0 ? null : generatePersonModel(person, accessLevel==2, ssoSessionId);
	}
    
    /**
     * 获取用户详细信息
     * @param loginName
     * @param ssoSessionId
     * @return
     * @throws ServiceException
     */
	public Person getPersonByLoginName(String loginName, String ssoSessionId) throws ServiceException, SoapException {
		PersonService personService = (PersonService)getSpringService("personService");
		com.yuanluesoft.jeaf.usermanage.pojo.Person person = personService.getPersonByLoginName(loginName);
		if(person==null) {
			return null;
		}
		//检查用户对其所在组织机构的权限
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		int accessLevel = checkPersonPopedom(person, sessionInfo);
		return accessLevel==0 ? null : generatePersonModel(person, accessLevel==2, ssoSessionId);
	}
	
	/**
	 * 检查对用户的操作权限,0:无权限,1:有访问权限,2:有管理权限
	 * @param person
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	private int checkPersonPopedom(com.yuanluesoft.jeaf.usermanage.pojo.Person person, SessionInfo sessionInfo) throws ServiceException, SoapException {
		if(sessionInfo.getLoginName().equals(person.getLoginName())) {
			return 2;
		}
		OrgService orgService = (OrgService)getSpringService("orgService");
		int index = 0;
		for(Iterator iterator = person.getSubjections().iterator(); iterator.hasNext();) {
			PersonSubjection subjection = (PersonSubjection)iterator.next();
			List popedoms = orgService.listDirectoryPopedoms(subjection.getOrgId(), sessionInfo);
			if(popedoms!=null && !popedoms.isEmpty()) {
				return index==0 && popedoms.contains("manager") ? 2 : 1;
			}
			index++;
		}
		return 0;
	}
	
	/**
	 * 检查对组织机构的操作权限,0:无权限,1:有访问权限,2:有管理权限
	 * @param orgIds
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	private int checkOrgsPopedom(String orgIds, SessionInfo sessionInfo) throws ServiceException, SoapException {
		OrgService orgService = (OrgService)getSpringService("orgService");
		String[] ids = orgIds.split(",");
		boolean manage = true;
		for(int i=0; i<ids.length; i++) {
			List popedoms = orgService.listDirectoryPopedoms(Long.parseLong(ids[i]), sessionInfo);
			if(popedoms==null || popedoms.isEmpty()) {
				return 0;
			}
			if(!popedoms.contains("manager")) {
				manage = false;
			}
		}
		return manage ? 2 : 1;
	}
	
	/**
	 * 生成用户模型
	 * @param person
	 * @param fullAccess
	 * @return
	 * @throws ServiceException
	 * @throws SoapException
	 */
	private Person generatePersonModel(com.yuanluesoft.jeaf.usermanage.pojo.Person person, boolean fullAccess, String ssoSessionId) throws ServiceException, SoapException {
		OrgService orgService = (OrgService)getSpringService("orgService");
		Person personModel = new Person();
		personModel.setId(person.getId()); //ID
		personModel.setName(person.getName()); //用户名
		personModel.setLoginName(person.getLoginName()); //登录用户名
		personModel.setType(PersonService.PERSON_TYPE_TITLES[person.getType()]); //用户类型,普通用户/教师/学生/学生家长
		personModel.setPriority(person.getPriority()); //优先级,用来做用户排序
		personModel.setSex(person.getSex()=='M' ? "男" : "女"); //性别,男/女
		personModel.setOrgName(orgService.getDirectoryFullName(((PersonSubjection)person.getSubjections().iterator().next()).getOrgId(), "/", "unit,school")); //所在组织机构
		personModel.setSubjectionOrgIds(ListUtils.join(person.getSubjections(), "orgId", ",", false)); //用户隶属的组织机构ID列表
		personModel.setSubjectionRoleIds(ListUtils.join(person.getSubjectionRoles(), "roleId", ",", false)); //用户隶属的角色/岗位ID列表
		personModel.setSupervisorIds(ListUtils.join(person.getSupervisors(), "supervisorId", ",", false)); //分管领导ID
		personModel.setSupervisorNames(ListUtils.join(person.getSupervisors(), "supervisor", ",", false)); //分管领导姓名
		if(!fullAccess) { //没有全部权限
			return personModel;
		}
		PersonService personService = (PersonService)getSpringService("personService");
		try {
			String personPassword = personService.decryptPassword(person.getId(), person.getPassword());
			personModel.setPassword(Encoder.getInstance().desBase64Encode(Encoder.getInstance().desBase64Encode(personPassword, ssoSessionId, null), "20050718", null)); //密码
		}
		catch(Exception e) {
			
		}
		personModel.setMailAddress(person.getMailAddress()); //邮箱地址
		personModel.setFamilyAddress(person.getFamilyAddress()); //家庭地址
		personModel.setCell(person.getMobile()); //手机
		personModel.setTelOffice(person.getTel()); //办公室电话
		personModel.setTelFamily(person.getTelFamily()); //家庭电话
		//获取用户头像
		Image portrait = personService.getPersonPortrait(person.getId(), false);
		personModel.setPortraitWidth(portrait.getWidth()); //头像高度
		personModel.setPortraitHeight(portrait.getHeight()); //头像宽度
		personModel.setPortraitUrl(portrait.getUrl());//头像URL
		return personModel;
	}
	
	/**
	 * 注册用户
	 * @param personId
	 * @param orgIds
	 * @param loginName
	 * @param name
	 * @param password
	 * @param sex
	 * @param mailAddress
	 * @param familyAddress
	 * @param cell
	 * @param telOffice
	 * @param telFamily
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void registPerson(long personId, String orgIds, String loginName, String name, String password, String sex, String mailAddress, String familyAddress, String cell, String telOffice, String telFamily, String ssoSessionId) throws ServiceException, SoapException {
		registOrModifyPerson(personId, orgIds, loginName, name, password, sex, mailAddress, familyAddress, cell, telOffice, telFamily, ssoSessionId);
	}
	
	/**
	 * 修改用户信息
	 * @param personId
	 * @param orgIds
	 * @param loginName
	 * @param name
	 * @param password
	 * @param sex 男/女/M/F
	 * @param mailAddress
	 * @param familyAddress
	 * @param cell
	 * @param telOffice
	 * @param telFamily
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void modifyPerson(long personId, String orgIds, String loginName, String name, String password, String sex, String mailAddress, String familyAddress, String cell, String telOffice, String telFamily, String ssoSessionId) throws ServiceException, SoapException {
		registOrModifyPerson(personId, orgIds, loginName, name, password, sex, mailAddress, familyAddress, cell, telOffice, telFamily, ssoSessionId);
	}

	/**
	 * 修改用户信息
	 * @param personId
	 * @param orgIds
	 * @param loginName
	 * @param name
	 * @param password
	 * @param sex 男/女/M/F
	 * @param mailAddress
	 * @param familyAddress
	 * @param cell
	 * @param telOffice
	 * @param telFamily
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	private void registOrModifyPerson(long personId, String orgIds, String loginName, String name, String password, String sex, String mailAddress, String familyAddress, String cell, String telOffice, String telFamily, String ssoSessionId) throws ServiceException, SoapException {
		//检查用户对其所在组织机构的权限
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		if(checkOrgsPopedom(orgIds, sessionInfo)<2) { //没有管理权限
			return;
		}
		PersonService personService = (PersonService)getSpringService("personService");
		com.yuanluesoft.jeaf.usermanage.pojo.Person person = (com.yuanluesoft.jeaf.usermanage.pojo.Person)personService.load(com.yuanluesoft.jeaf.usermanage.pojo.Person.class, personId);
		try {
			password = Encoder.getInstance().desBase64Decode(password, ssoSessionId, null);
		} 
		catch (Exception e) {
			Logger.exception(e);
		}
		if(person==null) { //新用户
			personService.addEmployee(personId, name, loginName, password, (sex.equals("男") || sex.equalsIgnoreCase("M") ? 'M' : 'F'), null, telFamily, cell, familyAddress, mailAddress, orgIds, sessionInfo.getUserId(), sessionInfo.getUserName());
		}
		else { //旧用户
			person.setName(name);
			person.setPassword(password);
			person.setSex(sex.equals("男") || sex.equalsIgnoreCase("M") ? 'M' : 'F');
			person.setMailAddress(mailAddress);
			person.setFamilyAddress(familyAddress);
			person.setMobile(cell);
			person.setTel(telOffice);
			person.setTelFamily(telFamily);
			personService.update(person);
			personService.updatePersonSubjection(personId, orgIds);
		}
	}
	
	/**
	 * 删除用户
	 * @param personId
	 */
	public void deletePerson(long personId, String ssoSessionId) throws ServiceException, SoapException {
		PersonService personService = (PersonService)getSpringService("personService");
		com.yuanluesoft.jeaf.usermanage.pojo.Person person = (com.yuanluesoft.jeaf.usermanage.pojo.Person)personService.load(com.yuanluesoft.jeaf.usermanage.pojo.Person.class, personId);
		if(person==null) {
			return;
		}
		//检查用户对其所在组织机构的权限
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		if(checkPersonPopedom(person, sessionInfo)<2) { //没有管理权限
			return;
		}
		personService.delete(person);
	}
	
	/**
	 * 注册组织机构
	 * @param orgId
	 * @param parentOrgId
	 * @param name
	 * @param fullName
	 * @param type
	 * @param priority
	 * @param linkedOrgId
	 * @param leaderIds
	 * @param leaderNames
	 * @param supervisorIds
	 * @param supervisorNames
	 * @param remark
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void registOrg(long orgId, long parentOrgId, String name, String fullName, String type, float priority, long linkedOrgId, String leaderIds, String leaderNames, String supervisorIds, String supervisorNames, String remark, String ssoSessionId) throws ServiceException, SoapException {
		registOrModifyOrg(orgId, parentOrgId, name, fullName, type, priority, linkedOrgId, leaderIds, leaderNames, supervisorIds, supervisorNames, remark, ssoSessionId);
	}
	
	/**
	 * 修改组织机构
	 * @param orgId
	 * @param parentOrgId
	 * @param name
	 * @param fullName
	 * @param type
	 * @param priority
	 * @param linkedOrgId
	 * @param leaderIds
	 * @param leaderNames
	 * @param supervisorIds
	 * @param supervisorNames
	 * @param remark
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void modifyOrg(long orgId, long parentOrgId, String name, String fullName, String type, float priority, long linkedOrgId, String leaderIds, String leaderNames, String supervisorIds, String supervisorNames, String remark, String ssoSessionId) throws ServiceException, SoapException {
		registOrModifyOrg(orgId, parentOrgId, name, fullName, type, priority, linkedOrgId, leaderIds, leaderNames, supervisorIds, supervisorNames, remark, ssoSessionId);
	}
	
	/**
	 * 注册或者修改组织机构
	 * @param orgId
	 * @param parentOrgId
	 * @param name
	 * @param fullName
	 * @param type
	 * @param priority
	 * @param linkedOrgId
	 * @param leaderIds
	 * @param leaderNames
	 * @param supervisorIds
	 * @param supervisorNames
	 * @param remark
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	private void registOrModifyOrg(long orgId, long parentOrgId, String name, String fullName, String type, float priority, long linkedOrgId, String leaderIds, String leaderNames, String supervisorIds, String supervisorNames, String remark, String ssoSessionId) throws ServiceException, SoapException {
		//检查用户对父组织机构的权限
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		if(checkOrgsPopedom("" + parentOrgId, sessionInfo)<2) { //没有管理权限
			return;
		}		
		OrgService orgService = (OrgService)getSpringService("orgService");
		orgService.addOrg(orgId, parentOrgId, name, fullName, type, priority, linkedOrgId, leaderIds, leaderNames, supervisorIds, supervisorNames, remark);
	}
	
	/**
	 * 删除组织机构
	 * @param orgId
	 * @param parentOrgId
	 * @param ssoSessionId
	 * @throws ServiceException
	 * @throws SoapException
	 */
	public void deleteOrg(long orgId, long parentOrgId, String ssoSessionId) throws ServiceException, SoapException {
		//检查用户对父组织机构的权限
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		if(checkOrgsPopedom("" + parentOrgId, sessionInfo)<2) { //没有管理权限
			return;
		}		
		OrgService orgService = (OrgService)getSpringService("orgService");
		orgService.delete(orgService.getOrg(orgId));
	}
	
	/**
	 * 获取用户列表
	 * @param orgId
	 * @param ssoSessionId
	 * @return
	 * @throws ServiceException
	 */
	public Person[] listPersons(long orgId, String ssoSessionId) throws ServiceException, SoapException {
		OrgService orgService = (OrgService)getSpringService("orgService");
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		List popedoms = orgService.listDirectoryPopedoms(0, sessionInfo);
		if(popedoms==null) {
			return null;
		}
		List orgPersons = orgService.listOrgPersons(orgId + "", null, false, true, 0, 0);
		if(orgPersons==null || orgPersons.isEmpty()) {
			return null;
		}
		Person[] persons = new Person[orgPersons.size()];
		for(int i=0; i<persons.length; i++) {
			persons[i] = generatePersonModel((com.yuanluesoft.jeaf.usermanage.pojo.Person)orgPersons.get(i), popedoms.contains("manager"), ssoSessionId);
		}
		return persons;
	}
    
    /**
     * 设置代理人
     * @param personLoginName
     * @param agentLoginNames
     * @param beginTime
     * @param endTime
     * @param source
     * @param ssoSessionId
     * @throws ServiceException
     */
    public void setAgents(String personLoginName, String agentLoginNames, Calendar beginTime, Calendar endTime, String source, String ssoSessionId) throws ServiceException, SoapException {
    	SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
    	PersonService personService = (PersonService)getSpringService("personService");
    	List persons = personService.listPersonsByLoginNames(personLoginName + "," + agentLoginNames);
    	com.yuanluesoft.jeaf.usermanage.pojo.Person person = (com.yuanluesoft.jeaf.usermanage.pojo.Person)persons.get(0);
    	if(!person.getLoginName().equals(personLoginName)) {
    		return;
    	}
    	if(checkPersonPopedom(person, sessionInfo)<2) {
    		return;
    	}
        ((AgentService)getSpringService("agentService")).setAgents(person.getId(), ListUtils.join(persons.subList(1, persons.size()), "id", ",", false), (beginTime==null ? null : new Timestamp(beginTime.getTimeInMillis())), (endTime==null ? null : new Timestamp(endTime.getTimeInMillis())), source);
    }
    
    /**
     * 移除代理人
     * @param personLoginName
     * @param source
     * @param ssoSessionId
     * @throws ServiceException
     */
    public void removeAgents(String personLoginName, String source, String ssoSessionId) throws ServiceException, SoapException {
    	PersonService personService = (PersonService)getSpringService("personService");
    	SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
    	List persons = personService.listPersonsByLoginNames(personLoginName);
    	com.yuanluesoft.jeaf.usermanage.pojo.Person person = (com.yuanluesoft.jeaf.usermanage.pojo.Person)persons.get(0);
    	if(checkPersonPopedom(person, sessionInfo)<2) {
    		return;
    	}
        ((AgentService)getSpringService("agentService")).removeAgents(person.getId(), source);
    }
	
	/**
	 * 获取网上注册用户详细信息
	 * @param loginName
	 * @return
	 * @throws ServiceException
	 */
	public RegisteredUser getRegisteredUserByLoginName(String loginName, String ssoSessionId) throws ServiceException, SoapException {
		OrgService orgService = (OrgService)getSpringService("orgService");
		SessionInfo sessionInfo = getSessionInfo(ssoSessionId);
		//或用户对根目录的权限
		List popedoms = orgService.listDirectoryPopedoms(0, sessionInfo);
		if(popedoms==null) {
			return null;
		}
		com.yuanluesoft.jeaf.usermanage.member.pojo.Member member = ((MemberService)getSpringService("memberService")).getMemberByLoginName(loginName);
	    RegisteredUser registeredUser = new RegisteredUser();
	    try {
			PropertyUtils.copyProperties(registeredUser, member);
		}
	    catch(Exception e) {
			
		}
	    return registeredUser;
	}
}