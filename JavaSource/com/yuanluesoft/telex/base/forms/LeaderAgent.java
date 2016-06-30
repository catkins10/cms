package com.yuanluesoft.telex.base.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class LeaderAgent extends ActionForm {
	private long leaderId; //领导/机构ID
	private String leader; //领导姓名/机构名称
	private long agentId; //待签收用户/组织机构ID
	private String agent; //待签收用户/组织机构名称
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}
	/**
	 * @param agent the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}
	/**
	 * @return the agentId
	 */
	public long getAgentId() {
		return agentId;
	}
	/**
	 * @param agentId the agentId to set
	 */
	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
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
}