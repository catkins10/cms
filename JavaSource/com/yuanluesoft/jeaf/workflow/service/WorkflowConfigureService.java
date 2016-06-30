package com.yuanluesoft.jeaf.workflow.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.resource.WorkflowResource;

/**
 * 工作流配置服务
 * @author linchuan
 *
 */
public interface WorkflowConfigureService {
	
	/**
	 * 创建新的工作流
	 * @param applicationName 应用名称
	 * @param formName 表单名称,如果为空,则自动使用应用的第一个表单
	 * @param workflowResourceFileName 工作流资源文件名称,默认为resource-config.xml
	 * @param notifyURL
	 * @param returnURL
	 * @param testURL
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void createWorkflow(String applicationName, String formName, String workflowResourceFileName, String notifyURL, String returnURL, String testURL, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 编辑工作流定义
	 * @param applicationName 应用名称
	 * @param formName 表单名称,如果为空,则自动使用应用的第一个表单
	 * @param workflowResourceFileName 工作流资源文件名称,默认为resource-config.xml
	 * @param workflowId
	 * @param notifyURL
	 * @param returnURL
	 * @param testURL
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void editWorkflow(String applicationName, String formName, String workflowResourceFileName, String workflowId, String notifyURL, String returnURL, String testURL, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;

	/**
	 * 编辑子流程
	 * @param applicationName 应用名称
	 * @param formName 表单名称,如果为空,则自动使用应用的第一个表单
	 * @param workflowResourceFileName 工作流资源文件名称,默认为resource-config.xml
	 * @param workflowId
	 * @param subFlowProcessId
	 * @param notifyURL
	 * @param returnURL
	 * @param response
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void editSubFlow(String applicationName, String formName, String workflowResourceFileName, String workflowId, String subFlowProcessId, String notifyURL, String returnURL, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 校验并处理流程配置通知请求
	 * @param configureCallback
	 * @param request
	 * @throws ServiceException
	 */
	public void processNotifyRequest(WorkflowConfigureCallback configureCallback, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 保存工作流资源定义,自定义应用时使用
	 * @param applicationName
	 * @param workflowResource
	 * @throws ServiceException
	 */
	public void saveWorkflowResourceDefine(String applicationName, WorkflowResource workflowResource) throws ServiceException;
}