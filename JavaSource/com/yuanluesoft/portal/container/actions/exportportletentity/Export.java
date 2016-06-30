package com.yuanluesoft.portal.container.actions.exportportletentity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.portal.container.service.PortletDefinitionService;

/**
 * 
 * @author linchuan
 *
 */
public class Export extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
        }
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	//检查用户对用户根目录的管理权限
    	if(!getOrgService().checkPopedom(0, "manager", sessionInfo)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	PortletDefinitionService portletDefinitionService = (PortletDefinitionService)getService("portletDefinitionService");
    	portletDefinitionService.exportPortletEntitiesAsXML(0, -1, sessionInfo, response);
    	return null;
    }
}