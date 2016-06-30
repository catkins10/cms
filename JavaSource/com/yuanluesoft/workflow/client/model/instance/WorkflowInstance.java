/*
 * Created on 2005-2-12
 *
 */
package com.yuanluesoft.workflow.client.model.instance;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowInstance implements Serializable {
	private String id; //ID
	private String workflowName; //流程名称
	private String workflowDefinitionId; //流程ID
	private WorkflowPackage workflowPackage;
	private Timestamp created; //创建时间
	private List processInstanceList; //过程实例列表
	private WorkflowInstanceState state; //状态
	private boolean testInstance; //是否测试实例
	private List visitors; //流程实例访问者
	
	/**
	 * 按工作项ID查找工作项
	 * @param workItemId
	 * @return
	 */
	public WorkItem getWorkItem(String workItemId) {
		try {
		for(Iterator iterator = processInstanceList.iterator(); iterator.hasNext();) {
			ProcessInstance processInstance = (ProcessInstance)iterator.next();
			for(Iterator iteratorActivity = processInstance.getActivityInstanceList().iterator(); iteratorActivity.hasNext();) {
				Object activityInstance = iteratorActivity.next();
				if(!(activityInstance instanceof ActivityInstance)) {
					continue;
				}
				WorkItem workItem = (WorkItem)ListUtils.findObjectByProperty(((ActivityInstance)activityInstance).getWorkItemList(), "id", workItemId);
				if(workItem!=null) {
					return workItem;
				}
			}
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
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
	 * @return Returns the processInstanceList.
	 */
	public List getProcessInstanceList() {
		return processInstanceList;
	}
	/**
	 * @param processInstanceList The processInstanceList to set.
	 */
	public void setProcessInstanceList(List processInstanceList) {
		this.processInstanceList = processInstanceList;
	}
	/**
	 * @return Returns the workflowDefineId.
	 */
	public String getWorkflowDefinitionId() {
		return workflowDefinitionId;
	}
	/**
	 * @param workflowDefineId The workflowDefineId to set.
	 */
	public void setWorkflowDefinitionId(String workflowDefineId) {
		this.workflowDefinitionId = workflowDefineId;
	}
	/**
	 * @return Returns the state.
	 */
	public WorkflowInstanceState getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(WorkflowInstanceState state) {
		this.state = state;
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
	 * @return Returns the workflowDefine.
	 */
	public WorkflowPackage getWorkflowPackage() {
		return workflowPackage;
	}
	/**
	 * @param workflowDefine The workflowDefine to set.
	 */
	public void setWorkflowPackage(WorkflowPackage workflowDefine) {
		this.workflowPackage = workflowDefine;
	}
	/**
	 * @return Returns the testInstance.
	 */
	public boolean isTestInstance() {
		return testInstance;
	}
	/**
	 * @param testInstance The testInstance to set.
	 */
	public void setTestInstance(boolean testInstance) {
		this.testInstance = testInstance;
	}
	/**
	 * @return Returns the visitors.
	 */
	public List getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors The visitors to set.
	 */
	public void setVisitors(List visitors) {
		this.visitors = visitors;
	}
}
