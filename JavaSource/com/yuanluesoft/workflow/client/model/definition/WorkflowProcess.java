/*
 * Created on 2005-1-17
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowProcess extends Element {
	private String accessLevel; //访问级别,主流程为PUBLIC,子流程为PRIVATE
	private String description; //描述
	
	private List activityList; //活动列表
	private List transitionList; //连接列表
	private WorkflowPackage workflowPackage; //所在的包
	
	/**
	 * @return Returns the accessLevel.
	 */
	public String getAccessLevel() {
		return accessLevel;
	}
	/**
	 * @param accessLevel The accessLevel to set.
	 */
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	/**
	 * @return Returns the activityList.
	 */
	public List getActivityList() {
		return activityList;
	}
	/**
	 * @param activityList The activityList to set.
	 */
	public void setActivityList(List activityList) {
		this.activityList = activityList;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the transitionList.
	 */
	public List getTransitionList() {
		return transitionList;
	}
	/**
	 * @param transitionList The transitionList to set.
	 */
	public void setTransitionList(List transitionList) {
		this.transitionList = transitionList;
	}
	/**
	 * @return Returns the workflowPackage.
	 */
	public WorkflowPackage getWorkflowPackage() {
		return workflowPackage;
	}
	/**
	 * @param workflowPackage The workflowPackage to set.
	 */
	public void setWorkflowPackage(WorkflowPackage workflowPackage) {
		this.workflowPackage = workflowPackage;
	}
}
