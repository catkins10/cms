/*
 * Created on 2005-2-8
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

import java.util.List;

/**
 *
 * 流程入口
 * @author LinChuan
 * 
 *
 */
public class WorkflowEntry {
	private String workflowId; //流程ID
	private String workflowName; //流程名称
	private List activityEntries; //允许打开的活动
	private boolean needValidateProgrammingParticipants; //是否需要检查用户是否是编程决定的办理人
	
	/**
	 * @return Returns the activityEntries.
	 */
	public List getActivityEntries() {
		return activityEntries;
	}
	/**
	 * @param activityEntries The activityEntries to set.
	 */
	public void setActivityEntries(List activityEntries) {
		this.activityEntries = activityEntries;
	}
	/**
	 * @return Returns the workflowId.
	 */
	public String getWorkflowId() {
		return workflowId;
	}
	/**
	 * @param workflowId The workflowId to set.
	 */
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	/**
	 * @return Returns the workflowName.
	 */
	public String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName The workflowName to set.
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	/**
	 * @return the needValidateProgrammingParticipants
	 */
	public boolean isNeedValidateProgrammingParticipants() {
		return needValidateProgrammingParticipants;
	}
	/**
	 * @param needValidateProgrammingParticipants the needValidateProgrammingParticipants to set
	 */
	public void setNeedValidateProgrammingParticipants(
			boolean needValidateProgrammingParticipants) {
		this.needValidateProgrammingParticipants = needValidateProgrammingParticipants;
	}
}