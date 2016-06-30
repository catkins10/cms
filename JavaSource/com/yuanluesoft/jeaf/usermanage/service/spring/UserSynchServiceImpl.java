/*
 * Created on 2006-3-13
 *
 */
package com.yuanluesoft.jeaf.usermanage.service.spring;


import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.usermanage.pojo.Agent;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.Role;
import com.yuanluesoft.jeaf.usermanage.pojo.RoleMember;
import com.yuanluesoft.jeaf.usermanage.service.UserSynchService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class UserSynchServiceImpl implements UserSynchService {
	private DatabaseService databaseService;
	private SoapPassport serviceSoapPassport; //SOAP身份验证
	private SessionService sessionService;
	private CryptService cryptService;

	/*
	 *  (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.UserSynchService#savePerson(long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String[], boolean, boolean, boolean)
	 */
	public void savePerson(long id, String loginName, String name, String password, String mailAddress, String familyAddress, String mobile, String tel, String telFamily, long departmentId, String otherDepartmentIds, boolean isHalt, boolean deleteDiaable, boolean preassign, int priority) throws ServiceException {
		Person person = (Person)databaseService.findRecordById(Person.class.getName(), id);
		boolean isNew = false;
		if(person==null) {
		    person = (Person)databaseService.findRecordByKey(Person.class.getName(), "loginName", loginName);
		    if(person!=null) {
		        databaseService.deleteRecord(person);
		        databaseService.flush();
		    }
			person = new Person();
			person.setId(id);
			isNew = true;
		}
		person.setLoginName(loginName);
		person.setName(name);
	    person.setPassword(cryptService.encrypt(password, "" + person.getId(), false));
        person.setMailAddress(mailAddress);
		person.setFamilyAddress(familyAddress);
		person.setMobile(mobile);
		person.setTel(tel);
		person.setTelFamily(telFamily);
		//TODO person.setDepartmentId(departmentId);
		//TODO person.setOtherDepartmentIds(otherDepartmentIds);
		person.setDeleteDisable(deleteDiaable ? '1' : '0');
		person.setPreassign(preassign ? '1' : '0');
		person.setPriority(priority);
		if(isNew) {
			databaseService.saveRecord(person);
		}
		else {
			databaseService.updateRecord(person);
		}
		try {
			sessionService.removeSessionInfo(person.getLoginName());
		}
		catch (SessionException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.UserSynchService#deletePerson(java.lang.String)
	 */
	public void deletePerson(long id) throws ServiceException {
		Person person = (Person)databaseService.findRecordById(Person.class.getName(), id);
		databaseService.deleteRecord(person);
		databaseService.flush();
		try {
			sessionService.removeSessionInfo(person.getLoginName());
		}
		catch (SessionException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.UserSynchService#saveRole(java.lang.String, java.util.List)
	 */
	public void saveRole(long id, String name, long[] memberIds) throws ServiceException {
		boolean isNew = false;
		Role role = (Role)databaseService.findRecordById(Role.class.getName(),  id);
		if(role==null) {
		    role = (Role)databaseService.findRecordByKey(Role.class.getName(), "name", name);
		    if(role!=null) {
		        databaseService.deleteRecord(role);
		        databaseService.flush();
		    }
			role = new Role();
			role.setId(id);
			isNew = true;
		}
		//TODO role.setName(name);
		if(isNew) {
			databaseService.saveRecord(role);
		}
		else {
			databaseService.updateRecord(role);
			databaseService.deleteRecordsByHql("from RoleMember RoleMember where RoleMember.roleId=" + role.getId());
		}
		//保存成员
		if(memberIds!=null) {
			for(int i=0; i<memberIds.length; i++) {
				RoleMember roleMember = new RoleMember();
				roleMember.setId(UUIDLongGenerator.generateId());
				roleMember.setMemberId(memberIds[i]);
				roleMember.setRoleId(role.getId());
				databaseService.saveRecord(roleMember);
			}
		}
		databaseService.flush();
		try {
			sessionService.removeAllSessionInfo();
		}
		catch (SessionException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.UserSynchService#deleteRole(java.lang.String)
	 */
	public void deleteRole(long id) throws ServiceException {
	    databaseService.deleteRecordById(Role.class.getName(), id);
		databaseService.flush();
		try {
			sessionService.removeAllSessionInfo();
		}
		catch (SessionException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.usermanage.service.UserSynchService#addAgents(long, java.util.List, java.sql.Timestamp, java.sql.Timestamp, java.lang.String)
     */
    public void addAgents(long personId, List agentIds, Timestamp beginTime, Timestamp endTime, String source) throws ServiceException {
        if(agentIds==null || agentIds.isEmpty()) {
            return;
        }
        if(beginTime==null) {
            try {
                beginTime = DateTimeUtils.parseTimestamp("2005-07-18 13:41:00", null);
            } catch (ParseException e) {
            }
        }
        if(endTime==null) {
            try {
                endTime = DateTimeUtils.parseTimestamp("2188-07-18 13:41:00", null);
            } catch (ParseException e) {
            }
        }
        //删除原有代理人
        removeAgents(personId, source);
        for(Iterator iterator = agentIds.iterator(); iterator.hasNext();) {
            Long agentId = (Long)iterator.next();
            Agent agent = new Agent();
            agent.setId(UUIDLongGenerator.generateId());
            agent.setPersonId(personId);
            agent.setAgentId(agentId.longValue());
            agent.setBeginTime(beginTime);
            agent.setEndTime(endTime);
            agent.setSource(source);
            databaseService.saveRecord(agent);
        }
    }

    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.usermanage.service.UserSynchService#removeAgents(long, java.lang.String)
     */
    public void removeAgents(long personId, String source) throws ServiceException {
        databaseService.deleteRecordsByHql("from Agent Agent where Agent.personId=" + personId + " and Agent.source='" + JdbcUtils.resetQuot(source) + "'");
    }
    /**
	 * @return Returns the databaseService.
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	/**
	 * @param databaseService The databaseService to set.
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	/**
	 * @return Returns the sessionService.
	 */
	public SessionService getSessionService() {
		return sessionService;
	}
	/**
	 * @param sessionService The sessionService to set.
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
    /**
     * @return Returns the cryptService.
     */
    public CryptService getCryptService() {
        return cryptService;
    }
    /**
     * @param cryptService The cryptService to set.
     */
    public void setCryptService(CryptService cryptService) {
        this.cryptService = cryptService;
    }
    /**
     * @return Returns the soapAuthorize.
     */
    public SoapPassport getServiceSoapPassport() {
        return serviceSoapPassport;
    }
    /**
     * @param soapAuthorize The soapAuthorize to set.
     */
    public void setServiceSoapPassport(SoapPassport soapAuthorize) {
        this.serviceSoapPassport = soapAuthorize;
    }
}
