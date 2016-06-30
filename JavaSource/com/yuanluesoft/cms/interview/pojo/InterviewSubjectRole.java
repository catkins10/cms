package com.yuanluesoft.cms.interview.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 在线访谈:角色的对应人员(interview_subject_role)
 * @author linchuan
 *
 */
public class InterviewSubjectRole extends Record {
	private long subjectId; //主题ID
	private String role; //角色名称
	private String roleMemberIds; //人员ID列表
	private String roleMemberNames; //人员姓名列表
	
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
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}
	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
}
