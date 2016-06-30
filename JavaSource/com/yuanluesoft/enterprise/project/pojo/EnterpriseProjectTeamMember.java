package com.yuanluesoft.enterprise.project.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:项目组成员(enterprise_project_team_member)
 * @author linchuan
 *
 */
public class EnterpriseProjectTeamMember extends Record {
	private long teamId; //项目组ID
	private long memberId; //组员ID
	private String memberName; //组员姓名
	private char isManager = '0'; //是否项目组负责人
	
	/**
	 * @return the memberId
	 */
	public long getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the memberName
	 */
	public String getMemberName() {
		return memberName;
	}
	/**
	 * @param memberName the memberName to set
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	/**
	 * @return the teamId
	 */
	public long getTeamId() {
		return teamId;
	}
	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	/**
	 * @return the isManager
	 */
	public char getIsManager() {
		return isManager;
	}
	/**
	 * @param isManager the isManager to set
	 */
	public void setIsManager(char isManager) {
		this.isManager = isManager;
	}
}