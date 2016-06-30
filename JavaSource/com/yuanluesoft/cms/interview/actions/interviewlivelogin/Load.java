package com.yuanluesoft.cms.interview.actions.interviewlivelogin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.interview.forms.InterviewLiveLogin;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends com.yuanluesoft.jeaf.sso.actions.login.Load {

	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InterviewLiveLogin loginForm = (InterviewLiveLogin)form;
		loginForm.setRedirect(request.getContextPath() + "/cms/interview/interviewLive.shtml?id=" + loginForm.getId() + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
		ActionForward actionForward = super.execute(mapping, form, request, response);
		if(loginForm.getUserName()!=null && loginForm.getUserName().startsWith("interviewGuests_")) {
			loginForm.setUserName(null);
		}
		return actionForward;
	}
}