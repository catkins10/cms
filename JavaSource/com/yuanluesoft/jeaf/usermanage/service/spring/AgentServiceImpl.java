/*
 * Created on 2006-7-5
 *
 */
package com.yuanluesoft.jeaf.usermanage.service.spring;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.usermanage.pojo.Agent;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.AgentService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class AgentServiceImpl implements AgentService {
    private DatabaseService databaseService;
	private SoapPassport serviceSoapPassport; //服务端SOAP验证
	private UserSynchClientList userSynchClientList;
	private PersonService personService;
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.AgentService#addAgents(long, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.String)
	 */
	public void setAgents(long personId, String agentIds, Timestamp beginTime, Timestamp endTime, String source) throws ServiceException {
        //删除原有代理人
        databaseService.deleteRecordsByHql("from Agent Agent where Agent.personId=" + personId + " and Agent.source='" + JdbcUtils.resetQuot(source) + "'");
		if(agentIds==null || agentIds.equals("")) {
            return;
        }
        if(beginTime==null) {
            try {
                beginTime = DateTimeUtils.parseTimestamp("2005-07-18 13:41:00", null);
            }
            catch (ParseException e) {
            	
            }
        }
        if(endTime==null) {
            try {
                endTime = DateTimeUtils.parseTimestamp("2188-07-18 13:41:00", null);
            }
            catch (ParseException e) {
            	
            }
        }
        //设置新的代理人
        List persons = personService.listPersons(agentIds==null || agentIds.equals("") ? personId + "" : personId + "," + agentIds);
        Iterator iterator = persons.iterator();
        Person person = (Person)iterator.next();
        for(; iterator.hasNext();) {
            Person agentPerson = (Person)iterator.next();
            if(agentPerson.getId()!=personId) {
            	Agent agent = new Agent();
            	agent.setId(UUIDLongGenerator.generateId());
            	agent.setPersonId(personId);
            	agent.setAgentId(agentPerson.getId());
            	agent.setBeginTime(beginTime);
            	agent.setEndTime(endTime);
            	agent.setSource(source);
            	databaseService.saveRecord(agent);
            }
        }
        //代理人同步
        userSynchClientList.addAgents(person, persons.subList(1, persons.size()), beginTime, endTime, source);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.AgentService#listAgents(long)
	 */
	public List listAgents(String personIds) throws ServiceException {
		return listAgents(personIds, null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.AgentService#listAgents(long, java.lang.String)
	 */
	public List listAgents(String personIds, String source) throws ServiceException {
		Timestamp now = DateTimeUtils.now();
        String hql = "select distinct Person from Agent Agent, Person Person" +
        			 " where Agent.personId in (" + JdbcUtils.validateInClauseNumbers(personIds) + ")" +
        			 " and Agent.agentId=Person.id" +
        			 " and Agent.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(now, null) + ")" +
        			 " and Agent.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(now, null) + ")" +
        			 (source==null ? "" : " and Agent.source='" + JdbcUtils.resetQuot(source) + "'");
        return databaseService.findRecordsByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.AgentService#removeAgents(long, java.lang.String)
	 */
	public void removeAgents(long personId, String source) throws ServiceException {
		databaseService.deleteRecordsByHql("from Agent Agent where Agent.personId=" + personId + " and Agent.source='" + JdbcUtils.resetQuot(source) + "'");
        //代理人同步
		List persons = personService.listPersons(personId + "");
        userSynchClientList.removeAgents((Person)persons.get(0), source);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.service.AgentService#isAgent(java.lang.String, java.lang.String)
	 */
	public boolean isAgent(String agentPersonIds, String toCheckPersonIds) throws ServiceException {
		Timestamp now = DateTimeUtils.now();
        String hql = "select Agent.id" +
        			 " from Agent Agent" +
        			 " where Agent.personId in (" + JdbcUtils.validateInClauseNumbers(toCheckPersonIds) + ")" +
        			 " and Agent.agentId in (" + JdbcUtils.validateInClauseNumbers(agentPersonIds) + ")" +
        			 " and Agent.beginTime<TIMESTAMP(" + DateTimeUtils.formatTimestamp(now, null) + ")" +
        			 " and Agent.endTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(now, null) + ")";
        return databaseService.findRecordByHql(hql)!=null;
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