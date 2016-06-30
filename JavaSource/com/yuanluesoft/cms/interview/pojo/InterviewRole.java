package com.yuanluesoft.cms.interview.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 在线访谈:站点角色定义(interview_role)
 * @author linchuan
 *
 */
public class InterviewRole extends Record {
	private long siteId; //站点ID
	private String role; //角色名称
	private String roleMemberIds; //默认的人员ID列表,每个访谈可以有自己的配置
	private String roleMemberNames; //默认的人员姓名列表
	
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the roleMemberIds
	 */
	public String getRoleMemberIds() {
		return roleMemberIds;
	}
	/**
	 * @param roleMemberIds the roleMemberIds to set
	 */
	public void setRoleMemberIds(String roleMemberIds) {
		this.roleMemberIds = roleMemberIds;
	}
	/**
	 * @return the roleMemberNames
	 */
	public String getRoleMemberNames() {
		return roleMemberNames;
	}
	/**
	 * @param roleMemberNames the roleMemberNames to set
	 */
	public void setRoleMemberNames(String roleMemberNames) {
		this.roleMemberNames = roleMemberNames;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}
