package com.yuanluesoft.cms.onlineservice.actions.admin.directory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.forms.admin.Directory;
import com.yuanluesoft.cms.onlineservice.forms.admin.MainDirectory;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowConfig extends DirectoryAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(!"result".equals(forward.getName())) {
    		return forward;
    	}
    	SessionInfo sessionInfo = getSessionInfo(request, response);
    	Directory directoryForm = (Directory)form;
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=onlineServiceDirectoryService&target=" + directoryForm.getWorkflowConfigTarget() + "&directoryId=" + directoryForm.getId();
    	String returnURL = Environment.getWebApplicationUrl() + "/cms/onlineservice/admin/" + (directoryForm instanceof MainDirectory ? "mainDirectory" : "directory") + ".shtml?act=edit&id=" + directoryForm.getId();
    	String testURL = null;
    	String workflowId = (String)PropertyUtils.getProperty(directoryForm, directoryForm.getWorkflowConfigTarget() + "WorkflowId");
    	if(workflowId==null || workflowId.equals("")) { //没有配置过
    		workflowConfigureService.createWorkflow("cms/onlineservice/" + directoryForm.getWorkflowConfigTarget(), "admin/" + directoryForm.getWorkflowConfigTarget(), null, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow("cms/onlineservice/" + directoryForm.getWorkflowConfigTarget(), "admin/" + directoryForm.getWorkflowConfigTarget(), null, workflowId, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}