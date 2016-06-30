package com.yuanluesoft.cms.leadermail.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class LeaderMailDepartment extends ActionForm {
	private String departments; //部门名称
	private long siteId; //绑定的站点ID
	
	/**
	 * @return the departments
	 */
	public String getDepartments() {
		return departments;
	}
	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(String departments) {
		this.departments = departments;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}