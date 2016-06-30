package com.yuanluesoft.bidding.project.signup.actions.listsignupprojects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.base.BiddingBaseAction;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BiddingBaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	//加载页面
    	PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("bidding/project/signup", "signUpProjects", request, response, false);
		return null;
    }
}