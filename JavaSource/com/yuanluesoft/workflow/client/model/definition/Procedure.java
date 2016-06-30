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
public class Procedure extends Element {
	private WorkflowProcess workflowProcess; //流程过程
	private Application procedureApplication; //过程
	//TODO:参数自动匹配,暂不实现参数列表
	
	/**
	 * @return Returns the procedureApplication.
	 */
	public Application getProcedureApplication() {
		return procedureApplication;
	}
	/**
	 * @param procedureApplication The procedureApplication to set.
	 */
	public void setProcedureApplication(Application procedureApplication) {
		this.procedureApplication = procedureApplication;
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
