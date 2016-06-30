package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.forms.admin.ServiceItem;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowConfig extends ServiceItemAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(!"result".equals(forward.getName())) {
    		return forward;
    	}
    	SessionInfo sessionInfo = getSessionInfo(request, response);
    	ServiceItem serviceItemForm = (ServiceItem)form;
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=onlineServiceDirectoryService&target=" + serviceItemForm.getWorkflowConfigTarget() + "&serviceItemId=" + serviceItemForm.getId();
    	String returnURL = Environment.getWebApplicationUrl() + "/cms/onlineservice/admin/serviceItem.shtml?act=edit&id=" + serviceItemForm.getId();
    	String testURL = null;
    	String workflowId = (String)PropertyUtils.getProperty(serviceItemForm, serviceItemForm.getWorkflowConfigTarget() + "WorkflowId");
    	if(workflowId==null || workflowId.equals("")) { //没有配置过
    		workflowConfigureService.createWorkflow("cms/onlineservice/" + serviceItemForm.getWorkflowConfigTarget(), "admin/" + serviceItemForm.getWorkflowConfigTarget(), null, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow("cms/onlineservice/" + serviceItemForm.getWorkflowConfigTarget(), "admin/" + serviceItemForm.getWorkflowConfigTarget(), null, workflowId, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}