package com.yuanluesoft.enterprise.project.actions.projecttype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.ProjectType;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowConfig extends ProjectTypeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(!"result".equals(forward.getName())) {
    		return forward;
    	}
    	
    	SessionInfo sessionInfo = getSessionInfo(request, response);
    	ProjectType projectTypeForm = (ProjectType)form;
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=enterpriseProjectService&projectTypeId=" + projectTypeForm.getId();
    	String returnURL = Environment.getWebApplicationUrl() + "/enterprise/project/projectType.shtml?act=edit&id=" + projectTypeForm.getId();
    	String testURL = Environment.getWebApplicationUrl() + "/enterprise/project/project.shtml?act=create";
    	if(projectTypeForm.getWorkflowId()==null || projectTypeForm.getWorkflowId().equals("")) { //没有配置过
    		workflowConfigureService.createWorkflow("enterprise/project", "project", null, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow("enterprise/project", "project", null, projectTypeForm.getWorkflowId(), notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}