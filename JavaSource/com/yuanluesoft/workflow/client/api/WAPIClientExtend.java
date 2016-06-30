/*
 * Created on 2005-2-7
 *
 */
package com.yuanluesoft.workflow.client.api;

import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.workflow.client.exception.WorkflowException;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;
import com.yuanluesoft.workflow.client.model.runtime.DataFieldValue;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowExit;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowInterface;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowSign;

/**
 * 
 * @author linchuan
 *
 */
public interface WAPIClientExtend {
	
	//********************************* 工作流定义相关API *********************************/
    /**
	 * 获取工作流定义
	 * @param workflowDefinitionId
	 * @return
	 * @throws WorkflowException
	 */
	public WorkflowPackage getWorkflowPackage(String workflowConfigurePassword, String workflowDefinitionId) throws WorkflowException;
	
	/**
	 * 创建流程配置许可证
	 * @param workflowResourceXML
	 * @param notifyURL
	 * @param returnURL
	 * @param testURL
	 * @param workflowDefinePassword
	 * @return
	 * @throws WorkflowException
	 */
	public String createWorkflowConfigurePassport(String workflowConfigurePassword, String workflowResourceXML, String notifyURL, String returnURL, String testURL) throws WorkflowException;
	
	/**
	 * 验证许可证有效性
	 * @param workflowDefinePassword
	 * @param passport
	 * @return
	 * @throws WorkflowException
	 */
	public boolean valideteWorkflowConfigurePassport(String workflowConfigurePassword, String passport) throws WorkflowException;
	
	/********************************* 工作流入口相关API *********************************/
	/**
	 * 获取流程入口
	 * @param workflowDefinitionId
	 * @return
	 * @throws WorkflowException
	 */
	public WorkflowEntry getWorkflowEntry(String workflowDefinitionId) throws WorkflowException;
	
	/**
	 * 新建流程,获取流程界面信息
	 * @param workflowDefinitionId
	 * @param activityDefinitionId
	 * @return
	 * @throws WorkflowException
	 */
	public WorkflowInterface previewWorkflowInterface(String workflowDefinitionId, String activityDefinitionId) throws WorkflowException;
	
	/**
	 * 新建测试流程,获取流程界面信息
	 * @param workflowDefinitionId
	 * @param activityDefinitionId
	 * @return
	 * @throws WorkflowException
	 */
	public WorkflowInterface previewTestWorkflowInterface(String workflowDefinitionId, String activityDefinitionId) throws WorkflowException;
	
	/**
	 * 获取指定工作项的用户界面
	 * @param workflowInstanceId
	 * @param workItemId
	 * @return
	 * @throws WorkflowException
	 */
	public WorkflowInterface getWorkflowInterface(String workflowInstanceId, String workItemId) throws WorkflowException;
	
	/********************************* 工作流出口相关API *********************************/
	/**
	 * 
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param workflowData
	 * @param approvalResult
	 * @return
	 * @throws WorkflowException
	 */
	public WorkflowExit getWorkflowExit(String workflowInstanceId, String workItemId, DataFieldValue[] dataFieldValues) throws WorkflowException;
	
	/**
	 * 在条件判断函数执行后继续获取流程出口
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param workflowData
	 * @param approvalResult
	 * @param workflowExit
	 * @return
	 * @throws WorkflowException
	 */
	public WorkflowExit retrieveWorkflowExit(String workflowInstanceId, String workItemId, DataFieldValue[] dataFieldValues, WorkflowExit workflowExit) throws WorkflowException;
	
	/********************************* 工作流实例相关API *********************************/
	/**
	 * 创建工作流实例,返回流程实例ID
	 * @param workflowId
	 * @param activityDefinitionId
	 * @param workflowEntryProgrammingVisitors
	 * @return
	 * @throws WorkflowException
	 */
	public String createWorkflowInstance(String workflowId, String activityDefinitionId, List workflowEntryProgrammingVisitors) throws WorkflowException;
	
	/**
	 * 创建工作流测试实例,返回流程实例ID
	 * @param workflowId
	 * @param activityDefinitionId
	 * @param workflowEntryProgrammingVisitors
	 * @return
	 * @throws WorkflowException
	 */
	public String createTestWorkflowInstance(String workflowId, String activityDefinitionId, List workflowEntryProgrammingVisitors) throws WorkflowException;
	
	/**
	 * 删除流程实例
	 * @param workflowInstanceId
	 * @throws WorkflowException
	 */
	public void removeWorkflowInstance(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 挂起流程实例
	 * @param workflowInstanceId
	 * @throws WorkflowException
	 */
	public void suspendWorkflowInstance(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 恢复流程实例
	 * @param workflowInstanceId
	 * @throws WorkflowException
	 */
	public void resumeWorkflowInstance(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 锁定工作流实例
	 * @param workflowInstanceId
	 * @throws WorkflowException
	 */
	public void lockWorkflowInstance(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 获取当前锁定工作流实例的用户名
	 * @param workflowInstanceId
	 * @throws WorkflowException
	 */
	public String getWorkflowInstanceLockPersonName(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 检查当前用户是否锁定了工作流实例
	 * @param workflowInstanceId
	 * @throws WorkflowException
	 */
	public boolean isLockWorkflowInstance(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 获取工作流实例访问者列表,beginTime不为空时,返回创建时间在beginTime之后的,否则返回全部
	 * @param workflowInstanceId
	 * @param beginTime
	 * @return
	 * @throws WorkflowException
	 */
	public List listWorkflowInstanceVisitors(String workflowInstanceId, Timestamp beginTime) throws WorkflowException;
	
	/**
	 * 解锁工作流实例
	 * @param workflowInstanceId
	 * @throws WorkflowException
	 */
	public void unlockWorkflowInstance(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 获取字段列表
	 * @param workflowInstanceId
	 * @return
	 * @throws WorkflowException
	 */
	public List listDataFields(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 创建工作流实例浏览许可证
	 * @param workflowInstanceId
	 * @return
	 * @throws WorkflowException
	 */
	public String createWorkflowInstanceViewPassport(String workflowInstanceId) throws WorkflowException;
	
	/********************************* 工作项相关API *********************************/
	/**
	 * 生成当前用户在指定工作流实例中的工作表,max==0时获取全部
	 * @param workflowInstanceId
	 * @param max
	 * @return
	 * @throws WorkflowException
	 */
	public List listRunningWorkItems(String workflowInstanceId, boolean selfOnly) throws WorkflowException;
	
	/**
	 * 完成工作项
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param workflowMessage
	 * @param dataFieldValues
	 * @param workflowExit
	 * @return
	 * @throws WorkflowException
	 */
	public WorkflowSign completeWorkItem(String workflowInstanceId, String workItemId, WorkflowMessage workflowMessage, DataFieldValue[] dataFieldValues, WorkflowExit workflowExit) throws WorkflowException;
	
	/**
	 * 转办
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param transmitParticipant
	 * @param workflowMessage
	 * @param dataFieldValues
	 * @throws WorkflowException
	 */
	public void transmitWorkItem(String workflowInstanceId, String workItemId, Element transmitParticipant, WorkflowMessage workflowMessage, DataFieldValue[] dataFieldValues) throws WorkflowException;
	
	/**
	 * 增加共同办理人
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param personIds
	 * @param personNames
	 * @param workflowMessage
	 * @param dataFieldValues
	 * @throws WorkflowException
	 */
	public void addParticipants(String workflowInstanceId, String workItemId, String personIds, String personNames, WorkflowMessage workflowMessage, DataFieldValue[] dataFieldValues) throws WorkflowException;

	/********************************* 工作流回退相关API *********************************/
	/**
	 * 获取工作项可回退的环节实例列表
	 * @param workflowInstanceId
	 * @param workItemId
	 * @return
	 * @throws WorkflowException
	 */
	public List listReverseActivityInstances(String workflowInstanceId, String workItemId) throws WorkflowException;
	
	/**
	 * 回退
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param reverseActivityInstanceId
	 * @param workflowMessage
	 * @return
	 * @throws WorkflowException
	 */
	public void reverse(String workflowInstanceId, String workItemId, String reverseActivityInstanceId, WorkflowMessage workflowMessage, DataFieldValue[] dataFieldValues) throws WorkflowException;
	
	/********************************* 工作流收回相关API *********************************/
	/**
	 * 获取流程收回列表
	 * @param workflowInstanceId
	 * @return
	 * @throws WorkflowException
	 */
	public List listWorkflowUndos(String workflowInstanceId) throws WorkflowException;
	
	/**
	 * 收回
	 * @param workflowInstanceId
	 * @param workflowUndoId
	 * @param undoReason TODO
	 * @param workflowMessage TODO
	 * @return
	 * @throws WorkflowException
	 */
	public void undo(String workflowInstanceId, String workflowUndoId, String undoReason, WorkflowMessage workflowMessage, DataFieldValue[] dataFieldValues) throws WorkflowException;
	
	/********************************* 其他API *********************************/
	/**
	 * 获取指定工作项必须完成的操作
	 * @param workflowInstanceId
	 * @param workItemId
	 * @throws WorkflowException
	 */
	public List listUndoneActions(String workflowInstanceId, String workItemId) throws WorkflowException;
	
	/**
	 * 预览流程环节必须完成的操作
	 * @param workflowDefinitionId
	 * @param activityDefinitionId
	 * @return
	 * @throws WorkflowException
	 */
	public List previewUndoneActions(String workflowDefinitionId, String activityDefinitionId) throws WorkflowException;
	
	/**
	 * 通知工作流引擎指定操作已完成
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param actionName
	 * @throws WorkflowException
	 */
	public void completeAction(String workflowInstanceId, String workItemId, String actionName) throws WorkflowException;
	
	/**
	 * 写操作日志
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param content
	 * @throws WorkflowException
	 */
	public void writeTransactLog(String workflowInstanceId, String workItemId, String logContent) throws WorkflowException;
	
	
	/****************************** 工作流实例访问者相关API *************************/
	/**
	 * 增加个人访问者
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param personId
	 * @param personName
	 * @param workflowMessage TODO
	 * @return
	 * @throws WorkflowException
	 */
	public boolean addPersonVisitor(String workflowInstanceId, String workItemId, String personId, String personName, WorkflowMessage workflowMessage) throws WorkflowException;
	
	/**
	 * 增加部门访问者
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param departmentId
	 * @param departmentName
	 * @param workflowMessage TODO
	 * @return
	 * @throws WorkflowException
	 */
	public boolean addDepartmentVisitor(String workflowInstanceId, String workItemId, String departmentId, String departmentName, WorkflowMessage workflowMessage) throws WorkflowException;
	
	/**
	 * 增加角色访问者
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param roleId
	 * @param roleName
	 * @param workflowMessage
	 * @return
	 * @throws WorkflowException
	 */
	public boolean addRoleVisitor(String workflowInstanceId, String workItemId, String roleId, String roleName, WorkflowMessage workflowMessage) throws WorkflowException;
	
	/**
	 * 删除访问者
	 * @param workflowInstanceId
	 * @param workItemId
	 * @param visitorId
	 * @throws WorkflowException
	 */
	public void deleteVisitor(String workflowInstanceId, String workItemId, String visitorId) throws WorkflowException;
}