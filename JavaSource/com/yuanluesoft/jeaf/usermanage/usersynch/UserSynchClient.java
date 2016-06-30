/*
 * Created on 2006-3-13
 *
 */
package com.yuanluesoft.jeaf.usermanage.usersynch;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.usermanage.pojo.ClassTeacher;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;

/**
 * 
 * @author linchuan
 *
 */
public interface UserSynchClient {
    
	/**
	 * 保存组织机构
	 * @param org
	 * @param newOrg
	 * @param oldOrgName
	 * @throws UserSynchException
	 */
	public void saveOrg(Org org, boolean newOrg) throws UserSynchException;
	
	/**
	 * 保存组织机构管理员
	 * @param org
	 * @param manager
	 */
	public void saveOrgManager(Org org, Person manager) throws UserSynchException;
	
	/**
	 * 删除组织机构管理员
	 * @param org
	 * @param manager
	 * @throws UserSynchException
	 */
	public void deleteOrgManager(Org org, Person manager) throws UserSynchException;
	
	/**
	 * 删除组织机构
	 * @param org
	 * @throws UserSynchException
	 */
	public void deleteOrg(Org org) throws UserSynchException;
	
	/**
	 * 保存用户
	 * @param person
	 * @param oldPersonName
	 * @param departmentName
	 * @param otherDepartmentNames
	 * @return
	 * @throws UserSynchException
	 */
	public Person savePerson(Person person, String oldPersonName) throws UserSynchException;
	
	/**
	 * 更新用户隶属的组织机构
	 * @param person
	 * @param subjectionOrgIds
	 * @throws UserSynchException
	 */
	public void updatePersonSubjections(Person person, String subjectionOrgIds) throws UserSynchException;
	
	/**
	 * 删除用户
	 * @param person
	 * @throws UserSynchException
	 */
	public void deletePerson(Person person) throws UserSynchException;
	
	/**
	 * 保存角色
	 * @param role
	 * @param oldRoleName
	 * @param memberIds
	 * @param memberNames
	 * @throws UserSynchException
	 */
	public void saveRole(Role role, String oldRoleName, String memberIds, String memberNames) throws UserSynchException;
	
	/**
	 * 删除角色
	 * @param role
	 * @throws UserSynchException
	 */
	public void deleteRole(Role role) throws UserSynchException;
	
	/**
	 * 增加代理人
	 * @param person
	 * @param agents: Person列表
	 * @param beginTime
	 * @param endTime
	 * @param source
	 * @throws UserSynchException
	 */
	public void addAgents(Person person, List agents, Timestamp beginTime, Timestamp endTime, String source) throws UserSynchException;
	
	/**
	 * 删除代理人
	 * @param person
	 * @param source
	 * @throws UserSynchException
	 */
	public void removeAgents(Person person, String source) throws UserSynchException;
	
	/**
	 * 增加班级老师
	 * @param classId
	 * @param teacher
	 * @throws UserSynchException
	 */
	public void saveClassTeacher(ClassTeacher classTeacher) throws UserSynchException;
	
	/**
	 * 删除班级老师
	 * @param classId
	 * @param teacher
	 * @throws UserSynchException
	 */
	public void deleteClassTeacher(ClassTeacher classTeacher) throws UserSynchException;
	
	/**
	 * 从班级删除学生
	 * @param student
	 * @param schoolClass
	 * @throws UserSynchException
	 */
	public void deleteStudentFromClass(Person student, Org schoolClass) throws UserSynchException;
}