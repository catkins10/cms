package com.yuanluesoft.traffic.busline.actions.admin.buslineimport;

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
import com.yuanluesoft.traffic.busline.dataimport.BusLineImporter;

/**
 * 
 * @author linchuan
 *
 */
public class DoImport extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	List acl = getAcl("traffic/busline", sessionInfo);
    	if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
    	BusLineImporter busLineImporter = (BusLineImporter)getService("busLineImporter");
    	busLineImporter.importBusLine();
    	response.getWriter().write("completed");
    	return null;
    }
}