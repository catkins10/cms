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
public class WorkItem extends com.yuanluesoft.workflow.client.model.wapi.WorkItem implements Serializable {
	private Timestamp created; //创建时间
	private List agentList; //代理人列表
	private List transactLogList; //办理日志列表
	private String subFlowInstanceId; //子流程实例ID
	private ProcessInstance subFlowInstance; //子流程实例
	private List todoActionList; //系统要求执行的操作列表
	private boolean isReverse; //是否被回退
	private boolean isUndo; //是否是取回
	private double urgeHours = -1; //催办周期,-1时使用流程环节定义的催办周期
	
	/**
	 * @return Returns the agentList.
	 */
	public List getAgentList() {
		return agentList;
	}
	/**
	 * @param agentList The agentList to set.
	 */
	public void setAgentList(List agentList) {
		this.agentList = agentList;
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
	 * @return Returns the subFlowInstance.
	 */
	public ProcessInstance getSubFlowInstance() {
		return subFlowInstance;
	}
	/**
	 * @param subFlowInstance The subFlowInstance to set.
	 */
	public void setSubFlowInstance(ProcessInstance subFlowInstance) {
		this.subFlowInstance = subFlowInstance;
	}
	/**
	 * @return Returns the subFlowInstanceId.
	 */
	public String getSubFlowInstanceId() {
		return subFlowInstanceId;
	}
	/**
	 * @param subFlowInstanceId The subFlowInstanceId to set.
	 */
	public void setSubFlowInstanceId(String subFlowInstanceId) {
		this.subFlowInstanceId = subFlowInstanceId;
	}
	/**
	 * @return Returns the transactLogList.
	 */
	public List getTransactLogList() {
		return transactLogList;
	}
	/**
	 * @param transactLogList The transactLogList to set.
	 */
	public void setTransactLogList(List transactLogList) {
		this.transactLogList = transactLogList;
	}
	/**
	 * @return Returns the todoActionList.
	 */
	public List getTodoActionList() {
		return todoActionList;
	}
	/**
	 * @param todoActionList The todoActionList to set.
	 */
	public void setTodoActionList(List todoActionList) {
		this.todoActionList = todoActionList;
	}
	/**
	 * @return the isReverse
	 */
	public boolean isReverse() {
		return isReverse;
	}
	/**
	 * @param isReverse the isReverse to set
	 */
	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}
	/**
	 * @return the isUndo
	 */
	public boolean isUndo() {
		return isUndo;
	}
	/**
	 * @param isUndo the isUndo to set
	 */
	public void setUndo(boolean isUndo) {
		this.isUndo = isUndo;
	}
	/**
	 * @return the urgeHours
	 */
	public double getUrgeHours() {
		return urgeHours;
	}
	/**
	 * @param urgeHours the urgeHours to set
	 */
	public void setUrgeHours(double urgeHours) {
		this.urgeHours = urgeHours;
	}
}
