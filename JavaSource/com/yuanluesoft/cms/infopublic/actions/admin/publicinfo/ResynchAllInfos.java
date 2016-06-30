package com.yuanluesoft.cms.infopublic.actions.admin.publicinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ResynchAllInfos extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	//检查用户是否根目录管理员
    	PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService");
    	if(!publicDirectoryService.checkPopedom(0, "manager", sessionInfo)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
    	publicInfoService.resynchAllInfos(true);
    	response.getWriter().write("completed.");
    	return null;
    }
}