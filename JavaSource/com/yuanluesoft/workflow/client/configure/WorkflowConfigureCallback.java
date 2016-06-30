package com.yuanluesoft.workflow.client.configure;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 工作流配置回调,处理流程配置的通知
 * @author linchuan
 *
 */
public interface WorkflowConfigureCallback {
	public static final String CONFIGURE_ACTION_NEW = "new";
	public static final String CONFIGURE_ACTION_UPDATE = "update";
	public static final String CONFIGURE_ACTION_DELETE = "delete";
	
	/**
	 * 处理流程配置通知
	 * @param workflowId
	 * @param workflowConfigureAction new/update/delete
	 * @param userId
	 * @param workflowPackage new/update时有效
	 * @param notifyRequest
	 * @throws Exception
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception;
}