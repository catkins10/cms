package com.yuanluesoft.cms.publicservice.actions.workflowsetting;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.publicservice.pojo.WorkflowSetting;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;

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
    	PublicService publicService = (PublicService)getService("publicService");
    	SiteService siteService = (SiteService)getService("siteService");
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService"); //工作流配置服务
    	
    	long siteId = RequestUtils.getParameterLongValue(request, "siteId");
       	String applicationName = RequestUtils.getParameterStringValue(request, "applicationName");
       
       	//权限控制
    	List acl = getAcl(applicationName, sessionInfo);
    	if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) && !siteService.checkPopedom(siteId, "manager", sessionInfo)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	
    	WorkflowSetting workflowSetting = publicService.getWorkflowSetting(applicationName, siteId, false);
    	
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=publicService&applicationName=" + applicationName + "&siteId=" + siteId;
    	String returnURL = ""; //直接关闭
    	String testURL = ""; //从公网提交,无法测试
    	if(workflowSetting==null) { //没有配置过
    		workflowConfigureService.createWorkflow(applicationName, null, null, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow(applicationName, null, null, "" + workflowSetting.getWorkflowId(), notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}