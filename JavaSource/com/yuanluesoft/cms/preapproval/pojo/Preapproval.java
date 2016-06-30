package com.yuanluesoft.cms.preapproval.pojo;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 预审件(cms_pre_approval)
 * @author linchuan
 *
 */
public class Preapproval extends PublicService {
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
