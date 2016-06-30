package com.yuanluesoft.cms.preapproval.forms.admin;

import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;


/**
 * 
 * @author linchuan
 *
 */
public class Preapproval extends PublicServiceAdminForm {
	private String projectName; //预审项目名称

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}