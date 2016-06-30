package com.yuanluesoft.cms.interview.actions.interviewlive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.interview.model.InterviewSessionInfo;
import com.yuanluesoft.cms.interview.pojo.InterviewSubject;
import com.yuanluesoft.cms.interview.service.InterviewService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
	
	/*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//检查会话
    	anonymousEnable = true;
    	InterviewSessionInfo sessionInfo;
    	try {
    		sessionInfo = (InterviewSessionInfo)getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	InterviewService interviewService = (InterviewService)getService("interviewService");
		InterviewSubject interviewSubject = interviewService.getInterviewSubject(RequestUtils.getParameterLongValue(request, "id"));
		if(interviewSubject==null) {
			Logger.error("This page " + RequestUtils.getRequestURL(request, true) + " does not exist or has been deleted.");
			response.getWriter().write("This page does not exist or has been deleted.");
			return null;
		}
		request.setAttribute("interviewSubject", interviewSubject);
		if(interviewSubject.getIsEnding()=='1') { //已经结束
			//重定向到历史访谈页面
			response.sendRedirect(request.getContextPath() + "/cms/interview/interview.shtml?id=" + interviewSubject.getId());
			return null;
		}
		//获取用户角色
		String role = interviewService.getInterviewRole(interviewSubject, sessionInfo);
		if(role==null) { //没有有效的角色
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
		String pageName = null;
		if(role.equals("网友")) {
			pageName = "interviewLive";
		}
		else if(role.equals("嘉宾")) {
			pageName = "interviewGuestsLive";
		}
		else if(role.equals("主持人")) {
			pageName = "interviewCompereLive";
		}
		else { //审核人
			pageName = "interviewApprovalLive";
		}
    	PageService pageService = (PageService)getService("interviewLivePageService");
    	pageService.writePage("cms/interview", pageName, request, response, false);
        return null;
    }
}