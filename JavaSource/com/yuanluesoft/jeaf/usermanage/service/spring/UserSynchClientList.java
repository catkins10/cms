/*
 * Created on 2006-3-14
 *
 */
package com.yuanluesoft.jeaf.usermanage.service.spring;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.usermanage.pojo.ClassTeacher;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient;
import com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchException;

/**
 * 
 * @author linchuan
 *
 */
public class UserSynchClientList implements UserSynchClient {
	private List userSynchClients;
	
	/**
	 * 是否需要同步
	 * @return
	 */
	public boolean isSynchRequired() {
		return userSynchClients!=null && !userSynchClients.isEmpty();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#saveOrg(com.yuanluesoft.jeaf.usermanage.pojo.UserDirectory, java.lang.String, java.lang.String)
	 */
	public void saveOrg(Org org, boolean newOrg) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).saveOrg(org, newOrg);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteOrg(com.yuanluesoft.jeaf.usermanage.pojo.UserDirectory, java.lang.String)
	 */
	public void deleteOrg(Org org) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).deleteOrg(org);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteOrgManager(com.yuanluesoft.jeaf.usermanage.pojo.UserDirectory, com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void deleteOrgManager(Org org, Person manager) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).deleteOrgManager(org, manager);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#saveOrgManager(com.yuanluesoft.jeaf.usermanage.pojo.UserDirectory, com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void saveOrgManager(Org org, Person manager) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).saveOrgManager(org, manager);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#savePerson(com.yuanluesoft.jeaf.usermanage.pojo.Person, com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public Person savePerson(Person person, String oldPersonName) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			UserSynchClient userSynchClient = (UserSynchClient)iterator.next();
			if(Logger.isDebugEnabled()) {
				Logger.debug("UserSynch: synch person " + person.getLoginName() + " by " + userSynchClient + "...");
			}
			person = userSynchClient.savePerson(person, oldPersonName);
			if(Logger.isDebugEnabled()) {
				Logger.debug("UserSynch: synch person " + person.getLoginName() + " by " + userSynchClient + " completed.");
			}
		}
		return person;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#updatePersonSubjections(com.yuanluesoft.jeaf.usermanage.pojo.Person, java.lang.String)
	 */
	public void updatePersonSubjections(Person person, String subjectionOrgIds) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).updatePersonSubjections(person, subjectionOrgIds);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deletePerson(com.yuanluesoft.jeaf.usermanage.pojo.Person)
	 */
	public void deletePerson(Person person) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).deletePerson(person);
		}
	}

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#saveRole(com.yuanluesoft.jeaf.usermanage.pojo.Role, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void saveRole(Role role, String oldRoleName, String memberIds, String memberNames) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).saveRole(role, oldRoleName, memberIds, memberNames);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteRole(com.yuanluesoft.jeaf.usermanage.pojo.Role)
	 */
	public void deleteRole(Role role) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).deleteRole(role);
		}
	}

    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#addAgents(com.yuanluesoft.jeaf.usermanage.pojo.Person, java.util.List, java.sql.Timestamp, java.sql.Timestamp, java.lang.String)
     */
    public void addAgents(Person person, List agents, Timestamp beginTime, Timestamp endTime, String source) throws UserSynchException {
        for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).addAgents(person, agents, beginTime, endTime, source);
		}
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#removeAgents(com.yuanluesoft.jeaf.usermanage.pojo.Person, java.lang.String)
     */
    public void removeAgents(Person person, String source) throws UserSynchException {
        for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).removeAgents(person, source);
		}
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteClassTeacher(com.yuanluesoft.jeaf.usermanage.pojo.ClassTeacher)
	 */
	public void deleteClassTeacher(ClassTeacher classTeacher) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).deleteClassTeacher(classTeacher);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#saveClassTeacher(com.yuanluesoft.jeaf.usermanage.pojo.ClassTeacher)
	 */
	public void saveClassTeacher(ClassTeacher classTeacher) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).saveClassTeacher(classTeacher);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.usersynch.UserSynchClient#deleteStudentFromClass(com.yuanluesoft.jeaf.usermanage.pojo.Person, com.yuanluesoft.jeaf.usermanage.pojo.Org)
	 */
	public void deleteStudentFromClass(Person student, Org schoolClass) throws UserSynchException {
		for(Iterator iterator = userSynchClients.iterator(); iterator.hasNext();) {
			((UserSynchClient)iterator.next()).deleteStudentFromClass(student, schoolClass);
		}
	}

	/**
	 * @return Returns the userSynchClients.
	 */
	public List getUserSynchClients() {
		return userSynchClients;
	}
	/**
	 * @param userSynchClients The userSynchClients to set.
	 */
	public void setUserSynchClients(List userSynchClients) {
		this.userSynchClients = userSynchClients;
	}
}