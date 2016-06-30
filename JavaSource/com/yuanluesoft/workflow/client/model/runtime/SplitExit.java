/*
 * Created on 2006-4-17
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;


/**
 *
 * @author LinChuan
 * 类型为"同时启动"的流程出口
 *
 */
public class SplitExit extends BaseExit {
	private WorkflowExit splitWorkflowExit; //并行开始的流程出口

	/**
	 * @return Returns the splitWorkflowExit.
	 */
	public WorkflowExit getSplitWorkflowExit() {
		return splitWorkflowExit;
	}
	/**
	 * @param splitWorkflowExit The splitWorkflowExit to set.
	 */
	public void setSplitWorkflowExit(WorkflowExit splitWorkflowExit) {
		this.splitWorkflowExit = splitWorkflowExit;
	}
}
