/*
 * Created on 2007-4-11
 *
 */
package com.yuanluesoft.jeaf.usermanage.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * @author Administrator
 *
 */
public class Role extends Record {
	private long orgId;
	private String roleName;
	private int isPost;
	private Timestamp lastModified; //最后修改时间
	private String remark; //备注
	private Set members;
	
	/**
	 * @return Returns the orgId.
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return Returns the roleName.
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName The roleName to set.
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	 * @return the isPost
	 */
	public int getIsPost() {
		return isPost;
	}
	/**
	 * @param isPost the isPost to set
	 */
	public void setIsPost(int isPost) {
		this.isPost = isPost;
	}
	/**
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
