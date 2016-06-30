package com.yuanluesoft.cms.interview.actions.interviewlive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class LiveUpdate extends BaseAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(request.getParameter("target").equals("toApprovalSpeaks")) { //更新待审核发言,做会话检查
	    	try {
	    		getSessionInfo(request, response);
	    	}
	    	catch(SessionException se) {
	    		response.getWriter().write("<html><body onload=\"top.location.reload();\"></body></html>");
	    		return null;
	    	}
    	}
    	PageService pageService = (PageService)getService("interviewLiveUpdatePageService");
    	pageService.writePage("cms/interview", request.getParameter("pageName"), request, response, false);
        return null;
    }
}