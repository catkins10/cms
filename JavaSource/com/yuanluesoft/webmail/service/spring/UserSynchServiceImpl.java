/*
 * Created on 2006-5-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.service.spring;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.usermanage.pojo.ClassTeacher;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient;
import com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchException;
import com.yuanluesoft.webmail.model.MailServer;
import com.yuanluesoft.webmail.model.MailSession;
import com.yuanluesoft.webmail.service.mailservice.MailService;

/**
 *
 * @author linchuan
 *
 */
public class UserSynchServiceImpl implements UserSynchClient {
    private MailServer innerMailServer; //内置的邮件服务器
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#savePerson(com.yuanluesoft.jeaf.usermanage.pojo.Person, java.lang.String)
	 */
	public Person savePerson(Person person, String oldPersonName) throws UserSynchException {
		if(PersonService.PERSON_NOT_LOGIN_PASSWORD.equals(person.getPassword())) { //用户不需要登录系统,只读
			return person;
		}
		MailSession manageSession = null;
		try {
			manageSession = innerMailServer.getMailService().login(null, innerMailServer.getServerHost(), innerMailServer.getManagePort(), innerMailServer.getManagerName(), innerMailServer.getManagerPassword(), MailService.MAIL_SESSION_TYPE_MANAGE);
			if(oldPersonName==null || person.getMailAddress()==null || person.getMailAddress().equals("")) { //旧用户名为空,新用户,或者邮件帐号为空
				//创建邮箱
				String mailAccount = innerMailServer.getMailService().createMailAccount(manageSession, person.getLoginName(), innerMailServer.getMailDomain(), person.getPassword());
				person.setMailAddress(mailAccount);
			}
			else {
				//修改密码
				innerMailServer.getMailService().changePassword(manageSession, person.getLoginName(), innerMailServer.getMailDomain(), person.getPassword());
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try {
				innerMailServer.getMailService().logout(manageSession);
			} 
			catch (Exception e) {
				
			}
		}
		return person;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deletePerson(com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void deletePerson(Person person) throws UserSynchException {
		MailSession manageSession = null;
		try {
			manageSession = innerMailServer.getMailService().login(null, innerMailServer.getServerHost(), innerMailServer.getManagePort(), innerMailServer.getManagerName(), innerMailServer.getManagerPassword(), MailService.MAIL_SESSION_TYPE_MANAGE);
			//删除邮件帐号
			innerMailServer.getMailService().removeMailAccount(manageSession, person.getLoginName(), innerMailServer.getMailDomain());
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try {
				innerMailServer.getMailService().logout(manageSession);
			} 
			catch (Exception e) {
				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#addAgents(com.yuanluesoft.jeaf.usermanage.pojo.Person, java.util.List, java.sql.Timestamp, java.sql.Timestamp, java.lang.String)
	 */
	public void addAgents(Person person, List agents, Timestamp beginTime, Timestamp endTime, String source) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteClassTeacher(com.yuanluesoft.jeaf.usermanage.pojo.ClassTeacher)
	 */
	public void deleteClassTeacher(ClassTeacher classTeacher) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void deleteOrg(Org org) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteOrgManager(com.yuanluesoft.jeaf.usermanage.pojo.Org, com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void deleteOrgManager(Org org, Person manager) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteRole(com.yuanluesoft.jeaf.usermanage.pojo.Role)
	 */
	public void deleteRole(Role role) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteStudentFromClass(com.yuanluesoft.jeaf.usermanage.pojo.Person, com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void deleteStudentFromClass(Person student, Org schoolClass) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#removeAgents(com.yuanluesoft.jeaf.usermanage.pojo.Person, java.lang.String)
	 */
	public void removeAgents(Person person, String source) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#saveClassTeacher(com.yuanluesoft.jeaf.usermanage.pojo.ClassTeacher)
	 */
	public void saveClassTeacher(ClassTeacher classTeacher) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#saveOrg(com.yuanluesoft.jeaf.usermanage.pojo.Org, boolean)
	 */
	public void saveOrg(Org org, boolean newOrg) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#saveOrgManager(com.yuanluesoft.jeaf.usermanage.pojo.Org, com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void saveOrgManager(Org org, Person manager) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#saveRole(com.yuanluesoft.jeaf.usermanage.pojo.Role, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void saveRole(Role role, String oldRoleName, String memberIds, String memberNames) throws UserSynchException {
		//不需要实现
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#updatePersonSubjections(com.yuanluesoft.jeaf.usermanage.pojo.Person, java.lang.String)
	 */
	public void updatePersonSubjections(Person person, String subjectionOrgIds) throws UserSynchException {
		//UNICOM不需要实现
	}

	/**
	 * @return the innerMailServer
	 */
	public MailServer getInnerMailServer() {
		return innerMailServer;
	}
	
	/**
	 * @param innerMailServer the innerMailServer to set
	 */
	public void setInnerMailServer(MailServer innerMailServer) {
		this.innerMailServer = innerMailServer;
	}
}