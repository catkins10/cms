/*
 * Created on 2005-1-17
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.workflow.client.model.resource.Application;

/**
 * 
 * @author linchuan
 *
 */
public class Decision extends Element {
	private WorkflowProcess workflowProcess; //流程过程
	private Application decisionApplication;
	
	/**
	 * @return Returns the decisionApplication.
	 */
	public Application getDecisionApplication() {
		return decisionApplication;
	}
	/**
	 * @param decisionApplication The decisionApplication to set.
	 */
	public void setDecisionApplication(Application decisionApplication) {
		this.decisionApplication = decisionApplication;
	}
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
