/*
 * Created on 2005-1-21
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import com.yuanluesoft.jeaf.graphicseditor.model.extend.ConfigureExtend;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowProcessExtend extends ConfigureExtend {
	private String workflowProcessId;
	
	/**
	 * @return Returns the workflowProcessId.
	 */
	public String getWorkflowProcessId() {
		return workflowProcessId;
	}
	/**
	 * @param workflowProcessId The workflowProcessId to set.
	 */
	public void setWorkflowProcessId(String workflowProcessId) {
		this.workflowProcessId = workflowProcessId;
	}
}
