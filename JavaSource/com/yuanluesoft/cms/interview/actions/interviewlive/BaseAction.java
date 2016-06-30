package com.yuanluesoft.cms.interview.actions.interviewlive;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.cms.interview.model.InterviewSessionInfo;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class BaseAction extends com.yuanluesoft.jeaf.action.BaseAction {
	
	public BaseAction() {
		super();
		sessionInfoClass = InterviewSessionInfo.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL(javax.servlet.http.HttpServletRequest)
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		return new Link(Environment.getWebApplicationSafeUrl() + "/cms/interview/interviewLiveLogin.shtml?id=" + request.getParameter("id") + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"), "utf-8");
	}
}
