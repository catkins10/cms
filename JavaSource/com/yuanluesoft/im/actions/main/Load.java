package com.yuanluesoft.im.actions.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
        	getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        PageService pageService = (PageService)getService("imPageService");
        pageService.writePage("im", "main", request, response, false);
        return null;
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.action.BaseAction#getLoginPageLink(javax.servlet.http.HttpServletRequest)
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		return new Link(Environment.getWebApplicationSafeUrl() +  "/im/login.shtml", "utf-8"); 
	}
}