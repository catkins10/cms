package com.yuanluesoft.workflow.client.configure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.workflow.client.WorkflowClient;
import com.yuanluesoft.workflow.client.exception.WorkflowException;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 * 工作流配置器,加密配置参数,调用工作流配置界面
 * @author linchuan
 *
 */
public class WorkflowConfigurator {

	/**
	 * 创建新的工作流
	 * @param workflowClient
	 * @param workflowConfigurePassword
	 * @param workflowResourceXML
	 * @param workflowConfigureURL
	 * @param notifyURL
	 * @param returnURL
	 * @param testURL
	 * @param response
	 * @throws WorkflowException
	 */
	public void createWorkflow(WorkflowClient workflowClient, String workflowConfigurePassword, String workflowResourceXML, String workflowConfigureURL, String notifyURL, String returnURL, String testURL, HttpServletResponse response) throws WorkflowException {
		writeConfigureRequest(workflowClient, workflowConfigurePassword, workflowResourceXML, workflowConfigureURL, notifyURL, returnURL, testURL, response, "createWorkflow", null);
	}
	
	/**
	 * 编辑工作流定义
	 * @param workflowClient
	 * @param workflowConfigurePassword
	 * @param workflowResourceXML
	 * @param workflowDefinitionId
	 * @param workflowConfigureURL
	 * @param notifyURL
	 * @param returnURL
	 * @param testURL
	 * @param response
	 * @throws WorkflowException
	 */
	public void editWorkflow(WorkflowClient workflowClient, String workflowConfigurePassword, String workflowResourceXML, String workflowDefinitionId, String workflowConfigureURL, String notifyURL, String returnURL, String testURL, HttpServletResponse response) throws WorkflowException {
		Map values = new HashMap();
		values.put("workflowId", workflowDefinitionId);
		writeConfigureRequest(workflowClient, workflowConfigurePassword, workflowResourceXML, workflowConfigureURL, notifyURL, returnURL, testURL, response, "editWorkflow", values);
	}

	/**
	 * 编辑子流程
	 * @param workflowClient
	 * @param workflowConfigurePassword
	 * @param workflowResourceXML
	 * @param workflowDefinitionId
	 * @param subFlowProcessId
	 * @param workflowConfigureURL
	 * @param notifyURL
	 * @param returnURL
	 * @param response
	 * @throws WorkflowException
	 */
	public void editSubFlow(WorkflowClient workflowClient, String workflowConfigurePassword, String workflowResourceXML, String workflowDefinitionId, String subFlowProcessId, String workflowConfigureURL, String notifyURL, String returnURL, HttpServletResponse response) throws WorkflowException {
		Map values = new HashMap();
		values.put("workflowId", workflowDefinitionId);
		values.put("subFlowProcessId", subFlowProcessId);
		writeConfigureRequest(workflowClient, workflowConfigurePassword, workflowResourceXML, workflowConfigureURL, notifyURL, returnURL, null, response, "editSubFlow", values);
	}
	
	/**
	 * 校验并处理流程配置通知请求
	 * @param workflowClient
	 * @param workflowConfigurePassword
	 * @param configureCallback
	 * @param request
	 * @throws WorkflowException
	 */
	public void processNotifyRequest(WorkflowClient workflowClient, String workflowConfigurePassword, WorkflowConfigureCallback configureCallback, HttpServletRequest request) throws WorkflowException {
		//校验通知请求
		Encoder encoder = Encoder.getInstance();
		String validateCode;
		String workflowId = request.getParameter("workflowId");
		String workflowAction = request.getParameter("workflowAction");
		long userId = Long.parseLong(request.getParameter("userId"));
		try {
			validateCode = encoder.md5Encode(encoder.desBase64Encode(request.getParameter("passport") + workflowAction + workflowId + userId, workflowConfigurePassword, null));
		}
		catch (Exception e) {
			throw new WorkflowException("validate failed");
		}
		if(!validateCode.equals(request.getParameter("validateCode"))) {
			throw new WorkflowException("validate failed");
		}
		//获取工作流定义
		WorkflowPackage workflowPackage = workflowClient.getWorkflowPackage(workflowConfigurePassword, workflowId);
		try {
			configureCallback.processWorkflowConfigureNotify(workflowId, request.getParameter("workflowAction"), userId, workflowPackage, request);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出一个流程配置请求
	 * @param workflowClient
	 * @param workflowConfigurePassword
	 * @param workflowResourceXML
	 * @param workflowConfigureURL
	 * @param notifyURL
	 * @param returnURL
	 * @param testURL
	 * @param response
	 * @param workflowConfigureAction
	 * @param values
	 * @throws WorkflowException
	 */
	private void writeConfigureRequest(WorkflowClient workflowClient, String workflowConfigurePassword, String workflowResourceXML, String workflowConfigureURL, String notifyURL, String returnURL, String testURL, HttpServletResponse response, String workflowConfigureAction, Map values) throws WorkflowException {
		String ticket = workflowClient.createWorkflowConfigurePassport(workflowConfigurePassword, workflowResourceXML, notifyURL, returnURL, testURL);
		try {
			String url = workflowConfigureURL + "?act=" + workflowConfigureAction + //流程操作
						 "&ticket=" + ticket; //许可证
			//输出其他字段
			for(Iterator iterator = (values==null ? null : values.keySet().iterator()); iterator!=null && iterator.hasNext();) {
				String name = (String)iterator.next();
				url += "&" + name + "=" + values.get(name);
			}
			response.sendRedirect(url);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new WorkflowException();
		}
	}
}