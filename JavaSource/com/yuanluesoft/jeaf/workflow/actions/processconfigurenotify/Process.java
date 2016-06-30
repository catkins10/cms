package com.yuanluesoft.jeaf.workflow.actions.processconfigurenotify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;

/**
 * 处理流程配置通知
 * @author linchuan
 *
 */
public class Process extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService");
    	WorkflowConfigureCallback configureCallback = (WorkflowConfigureCallback)getService(request.getParameter("callback"));
    	workflowConfigureService.processNotifyRequest(configureCallback, request);
        return null;
    }
}