/*
 * Created on 2005-2-7
 *
 */
package com.yuanluesoft.workflow.client.model.wapi;

import java.io.Serializable;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class WorkItem implements Cloneable, Serializable {
    private String name; //名称
    private String id; //工作项ID
    private ActivityInstance activityInstance; //活动实例
    private int priority; //优先级
    private Element participant; //办理人
    private WorkItemState state; //工作项状态
    
	/**
	 * @return Returns the activityDefinition.
	 */
	public Element getActivityDefinition() {
		return activityInstance.getActivityDefinition();
	}
	/**
	 * @return Returns the activityDefinitionId.
	 */
	public String getActivityDefinitionId() {
		return getActivityDefinition().getId();
	}
	/**
	 * @return Returns the activityInstance.
	 */
	public ActivityInstance getActivityInstance() {
		return activityInstance;
	}
	/**
	 * @param activityInstance The activityInstance to set.
	 */
	public void setActivityInstance(ActivityInstance activityInstance) {
		this.activityInstance = activityInstance;
	}
	/**
	 * @return Returns the activityInstanceId.
	 */
	public String getActivityInstanceId() {
		return activityInstance.getId();
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the participant.
	 */
	public Element getParticipant() {
		return participant;
	}
	/**
	 * @param participant The participant to set.
	 */
	public void setParticipant(Element participant) {
		this.participant = participant;
	}
	/**
	 * @return Returns the priority.
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return Returns the processInstance.
	 */
	public ProcessInstance getProcessInstance() {
		return activityInstance.getProcessInstance();
	}
	/**
	 * @return Returns the processInstanceId.
	 */
	public String getProcessInstanceId() {
		return getProcessInstance().getId();
	}
	/**
	 * @return Returns the state.
	 */
	public WorkItemState getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(WorkItemState state) {
		this.state = state;
	}
}
