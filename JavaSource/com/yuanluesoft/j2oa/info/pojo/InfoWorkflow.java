package com.yuanluesoft.j2oa.info.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 信息采编:流程配置(info_workflow)
 * @author linchuan
 *
 */
public class InfoWorkflow extends Record {
	private int type; //流程类型,1/稿件,2/刊物
	private long workflowId; //流程ID
	private String workflowName; //流程名称
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the workflowId
	 */
	public long getWorkflowId() {
		return workflowId;
	}
	/**
	 * @param workflowId the workflowId to set
	 */
	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}
	/**
	 * @return the workflowName
	 */
	public String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName the workflowName to set
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
}