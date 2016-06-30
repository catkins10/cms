/**
 * 
 */
package com.yuanluesoft.fet.tradestat.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * @author yuanluesoft
 *
 */
public class County extends ActionForm {
	private String code; //编码
	private String name; //名称
	private String loginName; //帐号
	private String password; //密码
	
	private RecordVisitorList queryUsers = new RecordVisitorList(); //查询权限
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the queryUsers
	 */
	public RecordVisitorList getQueryUsers() {
		return queryUsers;
	}
	/**
	 * @param queryUsers the queryUsers to set
	 */
	public void setQueryUsers(RecordVisitorList queryUsers) {
		this.queryUsers = queryUsers;
	}
}
