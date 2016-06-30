/*
 * Created on 2005-2-7
 *
 */
package com.yuanluesoft.workflow.client.model.wapi;

import java.io.Serializable;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class ActivityInstance implements Cloneable, Serializable {
    private String name; //活动实例名称
    private String id; //活动实例ID
    private String activityDefinitionId; //活动定义ID
    private Element activityDefinition; //活动定义
    private ProcessInstance processInstance; //过程实例
    private ActivityInstanceState state; //状态
    private int priority; //优先级
    private List participants; //办理人
    
    public String getStateTitle() {
    	return state.getStateTitle();
    }
	/**
	 * @return Returns the activityDefinitionId.
	 */
	public String getActivityDefinitionId() {
		return activityDefinitionId;
	}
	/**
	 * @param activityDefinitionId The activityDefinitionId to set.
	 */
	public void setActivityDefinitionId(String activityDefinitionId) {
		this.activityDefinitionId = activityDefinitionId;
	}
	/**
	 * @return Returns the activityDefinition.
	 */
	public Element getActivityDefinition() {
		return activityDefinition;
	}
	/**
	 * @param activityDefinition The activityDefinition to set.
	 */
	public void setActivityDefinition(Element activityDefinition) {
		this.activityDefinition = activityDefinition;
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
	 * @return Returns the participants.
	 */
	public List getParticipants() {
		return participants;
	}
	/**
	 * @param participants The participants to set.
	 */
	public void setParticipants(List participants) {
		this.participants = participants;
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
		return processInstance;
	}
	/**
	 * @param processInstance The processInstance to set.
	 */
	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	/**
	 * @return Returns the processInstanceId.
	 */
	public String getProcessInstanceId() {
		return processInstance.getId();
	}
	/**
	 * @return Returns the state.
	 */
	public ActivityInstanceState getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(ActivityInstanceState state) {
		this.state = state;
	}
}
