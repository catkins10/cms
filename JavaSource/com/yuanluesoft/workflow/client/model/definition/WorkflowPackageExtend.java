/*
 * Created on 2005-1-20
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowPackageExtend extends CloneableObject {
	String id;
	private List workflowProcessExtendList; //流程扩展列表
	private int nextProcessId; //下一个子流程ID
	/**
	 * @return Returns the workflowProcessExtendList.
	 */
	public List getWorkflowProcessExtendList() {
		return workflowProcessExtendList;
	}
	/**
	 * @param workflowProcessExtendList The workflowProcessExtendList to set.
	 */
	public void setWorkflowProcessExtendList(List workflowProcessExtendList) {
		this.workflowProcessExtendList = workflowProcessExtendList;
	}
	
	/**
	 * @return Returns the nextProcessId.
	 */
	public int getNextProcessId() {
		return nextProcessId;
	}
	/**
	 * @param nextProcessId The nextProcessId to set.
	 */
	public void setNextProcessId(int nextProcessId) {
		this.nextProcessId = nextProcessId;
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
}
