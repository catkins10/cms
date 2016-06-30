package com.yuanluesoft.cms.infopublic.actions.admin.directory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.forms.admin.ArticleDirectory;
import com.yuanluesoft.cms.infopublic.forms.admin.Directory;
import com.yuanluesoft.cms.infopublic.forms.admin.DirectoryCategory;
import com.yuanluesoft.cms.infopublic.forms.admin.MainDirectory;
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
    	
    	Directory directoryForm = (Directory)form;
    	SessionInfo sessionInfo = getSessionInfo(request, response);
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	
    	String pageName;
    	if(directoryForm instanceof MainDirectory) {
    		pageName = "mainDirectory";
    	}
    	else if(directoryForm instanceof DirectoryCategory) {
    		pageName = "directoryCategory";
    	}
    	else if(directoryForm instanceof ArticleDirectory) {
    		pageName = "articleDirectory";
    	}
    	else {
    		pageName = "infoDirectory";
    	}
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=publicDirectoryService&directoryId=" + directoryForm.getId();
    	String returnURL = Environment.getWebApplicationUrl() + "/cms/infopublic/admin/" + pageName + ".shtml?act=edit&id=" + directoryForm.getId();
    	String testURL = Environment.getWebApplicationUrl() + "/cms/infopublic/admin/info.shtml?act=create&directoryId=" + directoryForm.getId();
    	if(directoryForm.getWorkflowId()==0) { //没有配置过
    		workflowConfigureService.createWorkflow("cms/infopublic", "admin/publicInfo", null, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow("cms/infopublic", "admin/publicInfo", null, "" + directoryForm.getWorkflowId(), notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}