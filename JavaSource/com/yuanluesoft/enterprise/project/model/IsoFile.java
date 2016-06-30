package com.yuanluesoft.enterprise.project.model;

import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.workflow.client.model.instance.ActivityInstance;

/**
 * ISO文件,和工作项挂钩
 * @author linchuan
 *
 */
public class IsoFile {
	private ActivityInstance activityInstance; //对应的工作流环节实例
	private EnterpriseProjectTeam projectTeam; //对应的项目组,只对设计阶段有效
	private String attachmentType; //对应的附件类型
	
	public IsoFile(ActivityInstance activityInstance, EnterpriseProjectTeam projectTeam, String attachmentType) {
		super();
		this.activityInstance = activityInstance;
		this.projectTeam = projectTeam;
		this.attachmentType = attachmentType;
	}
	/**
	 * @return the activityInstance
	 */
	public ActivityInstance getActivityInstance() {
		return activityInstance;
	}
	/**
	 * @param activityInstance the activityInstance to set
	 */
	public void setActivityInstance(ActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}
	/**
	 * @return the attachmentType
	 */
	public String getAttachmentType() {
		return attachmentType;
	}
	/**
	 * @param attachmentType the attachmentType to set
	 */
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	/**
	 * @return the projectTeam
	 */
	public EnterpriseProjectTeam getProjectTeam() {
		return projectTeam;
	}
	/**
	 * @param projectTeam the projectTeam to set
	 */
	public void setProjectTeam(EnterpriseProjectTeam projectTeam) {
		this.projectTeam = projectTeam;
	}
}