/*
 * Created on 2005-2-6
 *
 */
package com.yuanluesoft.workflow.client.model.wapi;

import java.io.Serializable;

import com.yuanluesoft.workflow.client.model.definition.WorkflowProcess;

/**
 * 
 * @author linchuan
 *
 */
public class ProcessInstance implements Cloneable, Serializable {
	//目前，这是最小的元素列表，将来的版本将为这一结构提供可扩展性。
	private String name;
	private String id;
	private String processDefinitionId; //如"process-1"
	private WorkflowProcess processDefinition; 
	private ProcessInstanceState state;
	private int priority; //优先级
	//private String dataReference; //私有元素，包含供应商特有的信息
	//private List procParticipants; //最大为20 个长度为63 字符的参与者标识符

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
	 * @return Returns the processDefine.
	 */
	public WorkflowProcess getProcessDefinition() {
		return processDefinition;
	}
	/**
	 * @param processDefine The processDefine to set.
	 */
	public void setProcessDefinition(WorkflowProcess processDefinition) {
		this.processDefinition = processDefinition;
	}
	/**
	 * @return Returns the processDefineId.
	 */
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	/**
	 * @param processDefineId The processDefineId to set.
	 */
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	/**
	 * @return Returns the state.
	 */
	public ProcessInstanceState getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(ProcessInstanceState state) {
		this.state = state;
	}
}
