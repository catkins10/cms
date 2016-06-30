package com.yuanluesoft.cms.evaluation.model;

import java.sql.Timestamp;

/**
 * 被测评用户
 * @author linchuan
 *
 */
public class EvaluationTargetPerson {
	private long personId; //用户ID
	private String personName; //用户名
	private String orgName; //所在组织机构名称
	private Timestamp evluateTime; //测评时间
	
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the evluateTime
	 */
	public Timestamp getEvluateTime() {
		return evluateTime;
	}
	/**
	 * @param evluateTime the evluateTime to set
	 */
	public void setEvluateTime(Timestamp evluateTime) {
		this.evluateTime = evluateTime;
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
}