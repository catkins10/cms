package com.yuanluesoft.wechat.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 微信:用户分组(wechat_user_group)
 * @author linchuan
 *
 */
public class WechatUserGroup extends Record {
	private long unitId; //单位ID
	private String name; //分组名称
	private Set members; //成员列表
	
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
}