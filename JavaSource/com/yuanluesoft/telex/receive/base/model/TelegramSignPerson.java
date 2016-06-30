package com.yuanluesoft.telex.receive.base.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class TelegramSignPerson implements Serializable, Cloneable {
	private long personId; //用户ID
	private String personName; //用户姓名
	private long personOrgId; //所在单位
	private String personOrgFullName; //所在单位全称
	private String personOrgIds; //隶属的全部组织机构ID列表,包括上级部门
	private boolean isAgent; //是否代理人
	private String agentPersonOrOrgIds; //代理的用户或组织机构ID列表
	
	/**
	 * @return the isAgent
	 */
	public boolean isAgent() {
		return isAgent;
	}
	/**
	 * @param isAgent the isAgent to set
	 */
	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}
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
	/**
	 * @return the personOrgFullName
	 */
	public String getPersonOrgFullName() {
		return personOrgFullName;
	}
	/**
	 * @param personOrgFullName the personOrgFullName to set
	 */
	public void setPersonOrgFullName(String personOrgFullName) {
		this.personOrgFullName = personOrgFullName;
	}
	/**
	 * @return the personOrgId
	 */
	public long getPersonOrgId() {
		return personOrgId;
	}
	/**
	 * @param personOrgId the personOrgId to set
	 */
	public void setPersonOrgId(long personOrgId) {
		this.personOrgId = personOrgId;
	}
	/**
	 * @return the personOrgIds
	 */
	public String getPersonOrgIds() {
		return personOrgIds;
	}
	/**
	 * @param personOrgIds the personOrgIds to set
	 */
	public void setPersonOrgIds(String personOrgIds) {
		this.personOrgIds = personOrgIds;
	}
	/**
	 * @return the agentPersonOrOrgIds
	 */
	public String getAgentPersonOrOrgIds() {
		return agentPersonOrOrgIds;
	}
	/**
	 * @param agentPersonOrOrgIds the agentPersonOrOrgIds to set
	 */
	public void setAgentPersonOrOrgIds(String agentPersonOrOrgIds) {
		this.agentPersonOrOrgIds = agentPersonOrOrgIds;
	}
}
