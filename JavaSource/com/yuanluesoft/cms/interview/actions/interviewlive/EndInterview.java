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
 * 结束访谈
 * @author linchuan
 *
 */
public class EndInterview extends BaseAction {
    
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
    	interviewService.endInterview(RequestUtils.getParameterLongValue(request, "id"), sessionInfo);
    	//打开往期访谈页面
    	response.sendRedirect("interview.shtml?id=" + request.getParameter("id") + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
        return null;
    }
}