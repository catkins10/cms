/*
 * Created on 2005-2-6
 *
 */
package com.yuanluesoft.workflow.client.model.wapi;

import java.io.Serializable;


/**
 * 
 * @author linchuan
 *
 */
public class ConnectInfo implements Cloneable, Serializable {
	private long userId; //工作流参与者的标识符，工作流应用程序将代表他的利益进行操作。所指定的值可能代表人和设备等。该标识符通常用于安全检查和计账等
	private String userName; //用户名
	private long deparmentId; //所属部门ID
	private String deparmentIds; //所属部门及上级部门ID,用逗号分隔
	private String roleIds; //角色ID,用逗号分隔

	private String password;
	private String engineName; //工作流引擘的标识符，后续的调用将被定位到该工作流引擘。在正常情况下，对于某些工作流产品，该信息不是必须的，然而对于那些与多个工作流引擘交互的	工作流应用程序，却是必须的。这是一个符号名，可通过查找工具进行解析
	private String scope; //应用程序作用域的标识符。如果作用域无关紧要，那么这个域为空，或被怱略

	/**
	 * @return Returns the deparmentId.
	 */
	public long getDeparmentId() {
		return deparmentId;
	}
	/**
	 * @param deparmentId The deparmentId to set.
	 */
	public void setDeparmentId(long deparmentId) {
		this.deparmentId = deparmentId;
	}
	/**
	 * @return Returns the deparmentIds.
	 */
	public String getDeparmentIds() {
		return deparmentIds;
	}
	/**
	 * @param deparmentIds The deparmentIds to set.
	 */
	public void setDeparmentIds(String deparmentIds) {
		this.deparmentIds = deparmentIds;
	}
	/**
	 * @return Returns the engineName.
	 */
	public String getEngineName() {
		return engineName;
	}
	/**
	 * @param engineName The engineName to set.
	 */
	public void setEngineName(String engineName) {
		this.engineName = engineName;
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
	 * @return Returns the scope.
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * @param scope The scope to set.
	 */
	public void setScope(String scope) {
		this.scope = scope;
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
}
