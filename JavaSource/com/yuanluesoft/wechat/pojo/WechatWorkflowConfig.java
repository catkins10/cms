package com.yuanluesoft.wechat.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class WechatWorkflowConfig extends Record {
	private long orgId; //组织机构ID
	private String workflowId; //流程ID
	private String workflowName; //流程名称
	
	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the workflowId
	 */
	public String getWorkflowId() {
		return workflowId;
	}
	/**
	 * @param workflowId the workflowId to set
	 */
	public void setWorkflowId(String workflowId) {
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