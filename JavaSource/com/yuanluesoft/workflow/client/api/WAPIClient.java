/*
 * Created on 2005-2-6
 *
 */
package com.yuanluesoft.workflow.client.api;

import java.util.List;

import com.yuanluesoft.workflow.client.exception.WorkflowException;
import com.yuanluesoft.workflow.client.model.wapi.ActivityInstance;
import com.yuanluesoft.workflow.client.model.wapi.ActivityInstanceState;
import com.yuanluesoft.workflow.client.model.wapi.Attribute;
import com.yuanluesoft.workflow.client.model.wapi.ConnectInfo;
import com.yuanluesoft.workflow.client.model.wapi.Filter;
import com.yuanluesoft.workflow.client.model.wapi.ProcessDefinitionState;
import com.yuanluesoft.workflow.client.model.wapi.ProcessInstance;
import com.yuanluesoft.workflow.client.model.wapi.ProcessInstanceState;
import com.yuanluesoft.workflow.client.model.wapi.WorkItem;
import com.yuanluesoft.workflow.client.model.wapi.WorkItemState;

/**
 * 
 * @author linchuan
 *
 */
public interface WAPIClient {
	/****************WAPI 连接函数******************/
	//连接到WFM 引擘，以便完成一系列的交互
	public void connect(ConnectInfo connectInfo) throws WorkflowException;
	//在一系列交互后，断开与WFM 引擘的连接
	public void disconnect() throws WorkflowException;
	
	/***************WAPI 过程控制函数***************/
	//生成符合过滤器选择标准的所有过程定义的列表
	public List listProcessDefinitions(final Filter filter) throws WorkflowException;
	//生成符合过滤器选择标准的过程定义状态的列表
	public List listProcessDefinitionStates(final String processId, final Filter filter) throws WorkflowException;
	//改变所指定的过程定义的状态
	public void changeProcessDefinitionState(final String processDefinitionId, final ProcessDefinitionState newState) throws WorkflowException;
	//创建先前所定义的过程的一个实例,返回实例ID
	public String createProcessInstance(final String processDefinitionId, final String processInstanceName) throws WorkflowException;
	//启动指定的过程
	public String startProcess(final String processInstanceId) throws WorkflowException;
	//终止过程实例
	public void terminateProcessInstance(final String processInstanceId) throws WorkflowException;
	//生成所有符合过滤器选择标准的过程实例的状态列表
	public List listProcessInstanceStates(final String processInstanceId, final Filter filter) throws WorkflowException;
	//改变所指定的过程实例的状态
	public void changeProcessInstanceState(final String processInstanceId, final ProcessInstanceState newState) throws WorkflowException;
	//生成所有符合过滤器选择标准的过程实例的属性的列表
	public List getProcessInstanceAttributesList(final String processInstanceId, final Filter filter) throws WorkflowException;
	//返回过程实例属性的值、类型和长度，过程实例的属性由proc_inst_id 和attribute_name 参数指定
	public Attribute getProcessInstanceAttributeValue(final String processInstanceId, final String attributeName) throws WorkflowException;
	//为过程实例属性赋值
	public void assignProcessInstanceAttribute(final String processInstanceId, final String attributeName, final Object attributeValue)	throws WorkflowException;
	
	/*****************WAPI 活动控制函数*****************/
	//生成所有符合过滤器选择标准的活动实例的状态列表
	public List listActivityInstanceStates(final String processInstanceId, final String activityInstanceId, final Filter filter) throws WorkflowException;
	//改变所指定的活动实例的状态
	public void changeActivityInstanceState(final String processInstanceId, final String activityInstanceId, final ActivityInstanceState newState) throws WorkflowException;
	//生成所有符合过滤器选择标准的活动的属性列表
	public List listActivityInstanceAttributes(final String processInstanceId, final String activityInstanceId, final Filter filter) throws WorkflowException;
	//返回活动实例属性的值、类型和长度，活动实例的属性由proc_inst_id、pactivity_inst_id 和attribute_name 参数指定
	public Attribute getActivityInstanceAttributeValue(final String processInstanceId, final String activityInstanceId, final String attributeName) throws WorkflowException;
	//为活动实例的属性赋值
	public void assignActivityInstanceAttribute(final String processInstanceId,	final String activityInstanceId, final String attributeName, final Object attributeValue) throws WorkflowException;
	
	/******************WAPI 过程状态函数*****************/
	//指定并打开查询，生成所有符合过滤器选择标准的过程实例的列表
	public List listProcessInstances(final Filter filter) throws WorkflowException;
	//返回指定的过程实例记录
	public ProcessInstance getProcessInstance(final String processInstanceId) throws WorkflowException;
	
	/******************WAPI 活动状态函数*****************/
	//生成所有符合过滤器选择标准的活动实例的列表
	public List listActivityInstances(final Filter filter)	throws WorkflowException;
	//返回指定的活动实例的记录
	public ActivityInstance getActivityInstance(final String processInstanceId, final String activityInstanceId) throws WorkflowException;
	
	/******************WAPI 工作表函数*******************/
	//生成符合过滤器标准的工作表
	public List listWorkItems(final Filter filter) throws WorkflowException;
	//返回指定的工作项的记录
	public WorkItem getWorkItem(final String processInstanceId, final String workItemId) throws WorkflowException;
	//通知WFM 引擘，该工作项已被完成
	public void completeWorkItem(final String processInstanceId, final String workItemId) throws WorkflowException;
	//生成符合过滤器标准的工作项的状态列表
	public List listWorkItemStates(final String processInstanceId, final String workItemId, final Filter filter) throws WorkflowException;
	//改变所指定的工作项的状态
	public void changeWorkItemState(final String processInstanceId, final String workItemId, final WorkItemState newState) throws WorkflowException;
	//允许将工作项从一个工作流参与者的工作表中重新指派到另一个工作参与者的工作表中
	public void reassignWorkItem(final String sourceUser, final String targetUser, final String processInstanceId, final String workItemId) throws WorkflowException;
	//生成符合过滤器标准的工作项属性的列表
	public List listWorkItemAttributes(final String processInstanceId, final String workItemId, final Filter filter) throws WorkflowException;
	//返回工作项属性的值、类型和长度，工作项属性由pwork_item_id 参数指定
	public Attribute getWorkItemAttributeValue(final String processInstanceId, final String workItemId, final String attributeName) throws WorkflowException;
	//为工作项属性赋值
	public void assignWorkItemAttribute(final String processInstanceId,	final String workItemId, final String attributeName, final Object attributeValue) throws WorkflowException;
	
	/*******************WAPI 管理函数******************/
	//改变所指定的过程定义的实例的状态，该过程实例符合指定的过滤器标准
	public void changeProcessInstancesState(final String processInstanceId, final Filter filter, final ProcessInstanceState newState) throws WorkflowException;
	//改变活动实例的状态，该活动实例与过程定义的特定活动定义相关，且符合特定的过滤器标准
	public void changeActivityInstancesState(final String processDefinitionId, final String activityDefinitionId, final Filter filter, final ActivityInstanceState newState) throws WorkflowException;
	//终止指定的过程定义的过程实例，这些过程实例符合特定的过滤器标准
	public void terminateProcessInstances(final String processInstanceId, final Filter filter) throws WorkflowException;
	//为一组过程实例指定适当的属性，该组过程实例属于同一过程定义，且符合指定的过滤器标准
	public void assignProcessInstancesAttribute(final String processDefinitionId, final Filter filter, final String attributeName, final Object attributeValue) throws WorkflowException;
	//为一组活动实例指定适当的属性，该组活动实例属于同一过程定义，且符合指定的过滤器标准
	public void assignActivityInstancesAttribute(final String processDefinitionId, final String activityDefinitionId, final Filter filter, final String attributeName, final Object attributeValue) throws WorkflowException;
	//异常中止一组过程实例，而无需考虑其状态，该组过程实例对应于指定的过程定义，且符合指定的过滤器标准
	public void abortProcessInstances(final String processDefinitionId, final Filter filter) throws WorkflowException;
	//异常中止指定的过程实例，无需考虑其状态
	public void abortProcessInstance(final String processInstanceId) throws WorkflowException;
	
	/******************WAPI 应用程序调用函数******************/
	//TODO:工具代理(TA)
	//这些命令创建并终止与工作代理接口的连接,暂不支持
	//public void TAConnect();
	//public void TADisconnect();
	//命令工具代理启动或激活特定的应用程序,不实现,由工作流引擎自动调用
	//public void invokeApplication(final int toolAgentHandle, final String applicationName, final String procInstId, final String workItemId, final Object[] parameters, final int appMode) throws WorkflowException;
	//允许工作流系统检查打开的应用程序及其状态(运行,未决等等)
	//public Attribute[] requestAppStatus(final int toolAgentHandle, final String procInstId, final String workItemId, final int[] status) throws WorkflowException;
	//命令工具代理终止应用程序
	//public void terminateApp(final int toolAgentHandle, final String procInstId, final String workItemId) throws WorkflowException;
}
