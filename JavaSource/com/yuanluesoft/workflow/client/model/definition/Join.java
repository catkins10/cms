/*
 * Created on 2005-1-17
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Join extends Element {
	private WorkflowProcess workflowProcess; //流程过程
	/**
	 * @return Returns the workflowProcess.
	 */
	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}
	/**
	 * @param workflowProcess The workflowProcess to set.
	 */
	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}
}
