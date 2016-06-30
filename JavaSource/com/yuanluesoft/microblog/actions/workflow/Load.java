package com.yuanluesoft.microblog.actions.workflow;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;
import com.yuanluesoft.microblog.pojo.MicroblogWorkflowConfig;
import com.yuanluesoft.microblog.service.MicroblogService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	
    	long orgId = RequestUtils.getParameterLongValue(request, "orgId");
       	//权限控制
    	List acl = getAcl("microblog", sessionInfo);
    	if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) && !getOrgService().checkPopedom(orgId, "manager", sessionInfo)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	MicroblogService microblogService = (MicroblogService)getService("microblogService");
    	MicroblogWorkflowConfig workflowConfig = microblogService.getWorkflowConfig(orgId, false);
    	
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=microblogService&orgId=" + orgId;
    	String returnURL = ""; //直接关闭
    	String testURL = ""; //不测试
    	if(workflowConfig==null) { //没有配置过
    		workflowConfigureService.createWorkflow("microblog", "microblog", null, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow("microblog", "microblog", null, workflowConfig.getWorkflowId(), notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}