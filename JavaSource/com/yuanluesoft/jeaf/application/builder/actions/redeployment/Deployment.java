package com.yuanluesoft.jeaf.application.builder.actions.redeployment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Deployment extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionInfo sessionInfo;
        try {
        	sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        //检查权限
        List acl = getAcl("jeaf/application/builder", sessionInfo);
        if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
        	return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
        }
        ApplicationBuilder applicationBuilder = (ApplicationBuilder)getService("applicationBuilder");
        applicationBuilder.redeployment();
        response.getWriter().write("redeployment completed.");
        return null;
    }
}