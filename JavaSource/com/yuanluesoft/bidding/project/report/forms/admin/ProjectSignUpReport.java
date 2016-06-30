package com.yuanluesoft.bidding.project.report.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 项目报名报表
 * @author linchuan
 *
 */
public class ProjectSignUpReport extends ActionForm {
	private long projectId; //项目ID

	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
}