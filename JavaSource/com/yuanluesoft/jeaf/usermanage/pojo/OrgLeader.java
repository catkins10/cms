package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 组织机构:部门领导(user_org_leader)
 * @author linchuan
 *
 */
public class OrgLeader extends Record {
	private long orgId; //组织机构ID
	private String orgName; //组织机构名称
	private long leaderId; //领导ID
	private String leader; //领导姓名
	
	/**
	 * @return the leader
	 */
	public String getLeader() {
		return leader;
	}
	/**
	 * @param leader the leader to set
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}
	/**
	 * @return the leaderId
	 */
	public long getLeaderId() {
		return leaderId;
	}
	/**
	 * @param leaderId the leaderId to set
	 */
	public void setLeaderId(long leaderId) {
		this.leaderId = leaderId;
	}
	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
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
}
