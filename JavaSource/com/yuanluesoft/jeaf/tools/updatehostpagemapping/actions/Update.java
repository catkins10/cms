package com.yuanluesoft.jeaf.tools.updatehostpagemapping.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Update extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		//检查用户有没有用户管理的权限
		AccessControlService accessControlService = (AccessControlService)getService("accessControlService");
		List acl = accessControlService.getAcl("jeaf/usermanage", sessionInfo);
		if(!acl.contains("application_manager")) {
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
		SiteService siteService = (SiteService)getService("siteService");
		siteService.updateHostPageMapping();
		response.getWriter().write("completed.");
		return null;
    }
}