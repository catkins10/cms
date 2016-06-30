package com.yuanluesoft.enterprise.project.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ProjectType extends ActionForm {
	private String projectType; //项目类型,内部、外部、转包、外包
	private String workflowId; //流程ID
	private String workflowName; //流程名称
	private String contractNumberRule; //合同编号规则
	
	/**
	 * @return the projectType
	 */
	public String getProjectType() {
		return projectType;
	}
	/**
	 * @param projectType the projectType to set
	 */
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	/**
	 * @return the workflowId
	 */
	public String getWorkflowId() {
		return workflowId;
	}
	/**
	 * @param workflowId the workflowId to set
	 */
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	/**
	 * @return the workflowName
	 */
	public String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName the workflowName to set
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	/**
	 * @return the contractNumberRule
	 */
	public String getContractNumberRule() {
		return contractNumberRule;
	}
	/**
	 * @param contractNumberRule the contractNumberRule to set
	 */
	public void setContractNumberRule(String contractNumberRule) {
		this.contractNumberRule = contractNumberRule;
	}
}