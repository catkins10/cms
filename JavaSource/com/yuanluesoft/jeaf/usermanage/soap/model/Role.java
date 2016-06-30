package com.yuanluesoft.jeaf.usermanage.soap.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class Role implements Serializable {
	private long id; //ID
	private long orgId; //所在组织机构ID
	private String name; //名称
	private boolean isPost; //是否岗位
	private String memberIds; //成员ID列表,用逗号分隔
	private String memberNames; //成员姓名列表,用逗号分隔
	
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
	 * @return the isPost
	 */
	public boolean isPost() {
		return isPost;
	}
	/**
	 * @param isPost the isPost to set
	 */
	public void setPost(boolean isPost) {
		this.isPost = isPost;
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
	 * @return the memberNames
	 */
	public String getMemberNames() {
		return memberNames;
	}
	/**
	 * @param memberNames the memberNames to set
	 */
	public void setMemberNames(String memberNames) {
		this.memberNames = memberNames;
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
}