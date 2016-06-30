package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 用户:分管领导(user_person_supervisor)
 * @author linchuan
 *
 */
public class PersonSupervisor extends Record {
	private long personId; //用户ID
	private String personName; //用户名
	private long supervisorId; //分管领导ID
	private String supervisor; //分管领导姓名
	
	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	/**
	 * @return the supervisor
	 */
	public String getSupervisor() {
		return supervisor;
	}
	/**
	 * @param supervisor the supervisor to set
	 */
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	/**
	 * @return the supervisorId
	 */
	public long getSupervisorId() {
		return supervisorId;
	}
	/**
	 * @param supervisorId the supervisorId to set
	 */
	public void setSupervisorId(long supervisorId) {
		this.supervisorId = supervisorId;
	}
	/**
	 * @return the personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName the personName to set
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
}