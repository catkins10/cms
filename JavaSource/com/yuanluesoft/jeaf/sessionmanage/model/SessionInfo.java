/*
 * Created on 2004-12-21
 *
 */
package com.yuanluesoft.jeaf.sessionmanage.model;

import java.io.Serializable;

import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;

/**
 * 
 * @author linchuan
 *
 */
public class SessionInfo implements Serializable {
	private String loginName; //登录用户名
	private String password; //密码
	private String userName; //用户名
	private long userId; //用户ID
	private int userType; //用户类型,学生/家长/老师/普通用户
	private boolean internalUser; //是否内部用户
	private long departmentId; //所属部门ID
	private String departmentName; //所属部门名称
	private String departmentIds; //所属部门及上级部门ID,用逗号分隔
	private long unitId; //所在单位ID
	private String unitName; //所在单位名称
	private String roleIds; //角色ID,用逗号分隔
	
	public SessionInfo() {
		super();
	}

	public SessionInfo(String loginName, String userName, long userId, long departmentId, String departmentIds) {
		super();
		this.loginName = loginName;
		this.userName = userName;
		this.userId = userId;
		this.departmentId = departmentId;
		this.departmentIds = departmentIds;
	}

	/**
	 * 获取用户隶属的全部ID
	 * @return
	 */
	public String getUserIds() {
		return userId + "," + departmentIds + (roleIds==null ? "" : "," + roleIds);
	}
	
	/**
	 * 获取学习阶段
	 * @return
	 */
	public String getStudyStage() {
		if(departmentName.startsWith("高")) {
			return "高中";
		}
		if(departmentName.startsWith("初") || departmentName.startsWith("七") || departmentName.startsWith("八") || departmentName.startsWith("九")) {
			return "初中";
		}
		return "小学";
	}
	
	/**
	 * 是否匿名用户
	 * @return
	 */
	public boolean isAnonymous() {
		return SessionService.ANONYMOUS.equals(loginName);
	}

	/**
	 * @return Returns the deparmentId.
	 */
	public long getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param deparmentId The deparmentId to set.
	 */
	public void setDepartmentId(long deparmentId) {
		this.departmentId = deparmentId;
	}
	/**
	 * @return Returns the deparmentIds.
	 */
	public String getDepartmentIds() {
		return departmentIds;
	}
	/**
	 * @param deparmentIds The deparmentIds to set.
	 */
	public void setDepartmentIds(String deparmentIds) {
		this.departmentIds = deparmentIds;
	}
	/**
	 * @return Returns the loginName.
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName The loginName to set.
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * @return Returns the roleIds.
	 */
	public String getRoleIds() {
		return roleIds;
	}
	/**
	 * @param roleIds The roleIds to set.
	 */
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	/**
	 * @return Returns the userId.
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the userType
	 */
	public int getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(int userType) {
		this.userType = userType;
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
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the internalUser
	 */
	public boolean isInternalUser() {
		return internalUser;
	}

	/**
	 * @param internalUser the internalUser to set
	 */
	public void setInternalUser(boolean internalUser) {
		this.internalUser = internalUser;
	}
}
