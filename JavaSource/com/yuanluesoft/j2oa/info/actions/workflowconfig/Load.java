package com.yuanluesoft.j2oa.info.actions.workflowconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.info.pojo.InfoWorkflow;
import com.yuanluesoft.j2oa.info.service.InfoService;
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
    	//检查用户权限
    	List acl = getAccessControlService().getAcl("j2oa/info", sessionInfo);
    	if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	int type = RequestUtils.getParameterIntValue(request, "type");
    	//获取流程
    	InfoService infoService = (InfoService)getService("infoService");
    	InfoWorkflow infoWorkflow = infoService.getInfoWorkflow(type);
    	//发送流程配置请求
    	String notifyURL = Environment.getWebApplicationLocalUrl() + "/jeaf/workflow/processConfigureNotify.shtml?callback=infoService&type=" + type;
    	String returnURL = ""; //直接关闭
    	String testURL = ""; //不做测试
    	String formName = type==1 ? "info" : "magazine";
    	String workflowResourceFileName = type==1 ? "resource-config-info.xml" : "resource-config-magazine.xml"; 
    	WorkflowConfigureService workflowConfigureService = (WorkflowConfigureService)getService("workflowConfigureService");
    	if(infoWorkflow==null) { //没有配置过
    		workflowConfigureService.createWorkflow("j2oa/info", formName, workflowResourceFileName, notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	else {
    		workflowConfigureService.editWorkflow("j2oa/info", formName, workflowResourceFileName, "" + infoWorkflow.getWorkflowId(), notifyURL, returnURL, testURL, response, sessionInfo);
    	}
    	return null;
    }
}