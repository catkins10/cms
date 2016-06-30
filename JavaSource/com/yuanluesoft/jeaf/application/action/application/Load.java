package com.yuanluesoft.jeaf.application.action.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionInfo sessionInfo = null;
    	try {
    		sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        if(!sessionInfo.isInternalUser()) { //不是内部用户
        	return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
        }
        PageService pageService = (PageService)getService("applicationPageService");
        pageService.writePage("jeaf/usermanage", "application", request, response, false);
    	return null;
    }
}