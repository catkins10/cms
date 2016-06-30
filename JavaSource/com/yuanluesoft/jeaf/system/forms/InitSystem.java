package com.yuanluesoft.jeaf.system.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class InitSystem extends ActionForm {
	private String systemName;
	private String managerName;
	private String managerLoginName;
	private String managerPassword;
	
	/**
	 * @return the managerLoginName
	 */
	public String getManagerLoginName() {
		return managerLoginName;
	}
	/**
	 * @param managerLoginName the managerLoginName to set
	 */
	public void setManagerLoginName(String managerLoginName) {
		this.managerLoginName = managerLoginName;
	}
	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}
	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	/**
	 * @return the managerPassword
	 */
	public String getManagerPassword() {
		return managerPassword;
	}
	/**
	 * @param managerPassword the managerPassword to set
	 */
	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	/**
	 * @return the systemName
	 */
	public String getSystemName() {
		return systemName;
	}
	/**
	 * @param systemName the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
