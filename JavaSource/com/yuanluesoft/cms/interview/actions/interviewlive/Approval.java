package com.yuanluesoft.cms.interview.actions.interviewlive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.interview.model.InterviewSessionInfo;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Approval extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	InterviewSessionInfo sessionInfo;
    	try {
    		sessionInfo = (InterviewSessionInfo)getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		response.getWriter().write("<html><body onload=\"top.location.reload();\"></body></html>");
    		return null;
    	}
    	InterviewService interviewService = (InterviewService)getService("interviewService");
    	interviewService.approvalSpeak(RequestUtils.getParameterLongValue(request, "id"), "true".equals(request.getParameter("pass")), sessionInfo);
    	if("true".equals(request.getParameter("reload"))) {
    		response.getWriter().write("<html><body onload=\"top.location.reload(true);\"></body></html>");
    		return null;
    	}
    	else {
	    	String page = request.getParameter("page");
	    	response.sendRedirect("liveUpdate.shtml?target=toApprovalSpeaks&id=" + request.getParameter("subjectId") + "&pageName=" + request.getParameter("pageName") + (page==null ? "" : "&page=" + page) + "&seq=" + System.currentTimeMillis());
    	}
        return null;
    }
}