package com.yuanluesoft.fet.project.actions.admin.fairprojectreport;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fet.project.forms.admin.FairProjectReport;
import com.yuanluesoft.fet.project.service.FetProjectService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class WriteReport extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	//会话检查
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	//权限检查
    	List acl = getAccessControlService().getAcl("fet/project", sessionInfo);
    	if(!acl.contains("application_manager")) { //非管理员
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	FetProjectService fetProjectService = (FetProjectService)getService("fetProjectService");
    	FairProjectReport fairProjectReportForm = (FairProjectReport)form;
    	fetProjectService.exportFairPorjects(fairProjectReportForm.getFairName(), fairProjectReportForm.getFairNumber(), response);
    	return null;
    }
}