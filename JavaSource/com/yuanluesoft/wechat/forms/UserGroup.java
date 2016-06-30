package com.yuanluesoft.wechat.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class UserGroup extends ActionForm {
	private long unitId; //单位ID
	private String name; //分组名称
	private Set members; //成员列表
	
	//扩展属性
	private String memberIds; //成员ID列表
	private String memberNicknames; //成员昵称列表
	
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
	 * @return the members
	 */
	public Set getMembers() {
		return members;
	}
	/**
	 * @param members the members to set
	 */
	public void setMembers(Set members) {
		this.members = members;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the memberIds
	 */
	public String getMemberIds() {
		return memberIds;
	}
	/**
	 * @param memberIds the memberIds to set
	 */
	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}
	/**
	 * @return the memberNicknames
	 */
	public String getMemberNicknames() {
		return memberNicknames;
	}
	/**
	 * @param memberNicknames the memberNicknames to set
	 */
	public void setMemberNicknames(String memberNicknames) {
		this.memberNicknames = memberNicknames;
	}
}