/*
 * Created on 2005-2-13
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class ProcessInstance extends com.yuanluesoft.workflow.client.model.wapi.ProcessInstance implements Serializable {
	private List activityInstanceList; //活动实例列表
	private List transitionInstanceList; //连接实例列表
	private Timestamp created; //创建时间
	private WorkflowInstance workflowInstance; //工作流实例

	/**
	 * @return Returns the created.
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return Returns the activityInstanceList.
	 */
	public List getActivityInstanceList() {
		return activityInstanceList;
	}
	/**
	 * @param activityInstanceList The activityInstanceList to set.
	 */
	public void setActivityInstanceList(List activityInstanceList) {
		this.activityInstanceList = activityInstanceList;
	}
	/**
	 * @return Returns the transitionInstanceList.
	 */
	public List getTransitionInstanceList() {
		return transitionInstanceList;
	}
	/**
	 * @param transitionInstanceList The transitionInstanceList to set.
	 */
	public void setTransitionInstanceList(List transitionInstanceList) {
		this.transitionInstanceList = transitionInstanceList;
	}
	/**
	 * @return Returns the workflowInstance.
	 */
	public WorkflowInstance getWorkflowInstance() {
		return workflowInstance;
	}
	/**
	 * @param workflowInstance The workflowInstance to set.
	 */
	public void setWorkflowInstance(WorkflowInstance workflowInstance) {
		this.workflowInstance = workflowInstance;
	}
}
