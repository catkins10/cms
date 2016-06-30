package com.yuanluesoft.bidding.project.actions.configure.workflowconfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.WorkflowConfig;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;

/**
 * 
 * @author linchuan
 *
 */
public class DoWorkflowConfig extends WorkflowConfigAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(!"result".equals(forward.getName())) {
    		return forward;
    	}
    	SessionInfo sessionInfo = getSessionInfo(request, response);
    	WorkflowConfig configForm = (WorkflowConfig)form;
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=biddingProjectParameterService&workflowConfigId=" + configForm.getId();
    	String returnURL = Environment.getWebApplicationUrl() + "/bidding/project/admin/workflowConfig.shtml?act=edit&id=" + configForm.getId();
    	String testURL = null;
    	if(configForm.getWorkflowId()==0) { //没有配置过
    		workflowConfigureService.createWorkflow("bidding/project", "admin/project", null, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow("bidding/project", "admin/project", null, "" + configForm.getWorkflowId(), notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}