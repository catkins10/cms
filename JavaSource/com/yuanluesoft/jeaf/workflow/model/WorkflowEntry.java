package com.yuanluesoft.jeaf.workflow.model;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowEntry {
	private String workflowDefinitionId; //流程ID
	private String activityDefinitionId; //环节ID
	
	public WorkflowEntry() {
		super();
	}
	
	public WorkflowEntry(String workflowDefinitionId, String activityDefinitionId) {
		super();
		this.workflowDefinitionId = workflowDefinitionId;
		this.activityDefinitionId = activityDefinitionId;
	}

	/**
	 * @return the activityDefinitionId
	 */
	public String getActivityDefinitionId() {
		return activityDefinitionId;
	}
	/**
	 * @param activityDefinitionId the activityDefinitionId to set
	 */
	public void setActivityDefinitionId(String activityDefinitionId) {
		this.activityDefinitionId = activityDefinitionId;
	}
	/**
	 * @return the workflowDefinitionId
	 */
	public String getWorkflowDefinitionId() {
		return workflowDefinitionId;
	}
	/**
	 * @param workflowDefinitionId the workflowDefinitionId to set
	 */
	public void setWorkflowDefinitionId(String workflowDefinitionId) {
		this.workflowDefinitionId = workflowDefinitionId;
	}
}