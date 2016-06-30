package com.yuanluesoft.jeaf.usermanage.soap.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class Org implements Serializable {
	private long id; //ID
	private String name; //名称
	private String fullName; //全称
	private long parentOrgId; //上级目录ID
	private float priority; //优先级
	private String type; //类型
	private long linkedOrgId; //引用的组织机构ID
	private String leaderIds; //部门领导ID列表,用逗号分隔
	private String leaderNames; //部门领导姓名列表,用逗号分隔
	private String supervisorIds; //主管领导ID列表,用逗号分隔
	private String supervisorNames; //主管领导姓名列表,用逗号分隔
	
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the leaderIds
	 */
	public String getLeaderIds() {
		return leaderIds;
	}
	/**
	 * @param leaderIds the leaderIds to set
	 */
	public void setLeaderIds(String leaderIds) {
		this.leaderIds = leaderIds;
	}
	/**
	 * @return the leaderNames
	 */
	public String getLeaderNames() {
		return leaderNames;
	}
	/**
	 * @param leaderNames the leaderNames to set
	 */
	public void setLeaderNames(String leaderNames) {
		this.leaderNames = leaderNames;
	}
	/**
	 * @return the linkedOrgId
	 */
	public long getLinkedOrgId() {
		return linkedOrgId;
	}
	/**
	 * @param linkedOrgId the linkedOrgId to set
	 */
	public void setLinkedOrgId(long linkedOrgId) {
		this.linkedOrgId = linkedOrgId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parentOrgId
	 */
	public long getParentOrgId() {
		return parentOrgId;
	}
	/**
	 * @param parentOrgId the parentOrgId to set
	 */
	public void setParentOrgId(long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the supervisorIds
	 */
	public String getSupervisorIds() {
		return supervisorIds;
	}
	/**
	 * @param supervisorIds the supervisorIds to set
	 */
	public void setSupervisorIds(String supervisorIds) {
		this.supervisorIds = supervisorIds;
	}
	/**
	 * @return the supervisorNames
	 */
	public String getSupervisorNames() {
		return supervisorNames;
	}
	/**
	 * @param supervisorNames the supervisorNames to set
	 */
	public void setSupervisorNames(String supervisorNames) {
		this.supervisorNames = supervisorNames;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}