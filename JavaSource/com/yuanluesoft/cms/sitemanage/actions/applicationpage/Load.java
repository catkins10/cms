package com.yuanluesoft.cms.sitemanage.actions.applicationpage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		externalAction = true;
    	if("false".equals(request.getParameter("anonymous"))) { //禁止匿名
    		try {
    			getSessionInfo(request, response);
    		}
    		catch(SessionException se) {
    			return redirectToLogin(this, mapping, form, request, response, se, false);
    		}
    	}
    	PageService pageService = (PageService)getService("pageService");
    	pageService.writePage(request.getParameter("applicationName"), request.getParameter("pageName"), request, response, false);
    	return null;
    }
}