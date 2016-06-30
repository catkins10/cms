package com.yuanluesoft.bbs.base.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.bbs.base.forms.BbsViewForm;
import com.yuanluesoft.bbs.usermanage.model.BbsSessionInfo;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public abstract class BbsViewFormAction extends ViewFormAction {
	
	public BbsViewFormAction() {
		super();
		sessionInfoClass = BbsSessionInfo.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL()
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		return new Link(Environment.getWebApplicationSafeUrl() + "/bbs/usermanage/login.shtml?siteId=" + request.getParameter("siteId"), "utf-8"); 
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.initForm(viewForm, request, sessionInfo);
		BbsViewForm bbsViewForm = (BbsViewForm)viewForm;
		bbsViewForm.setAnonymousEnable(anonymousEnable);
		if(bbsViewForm.getRequestUrl()==null) {
			bbsViewForm.setRequestUrl(RequestUtils.getRequestURL(request, true));
			bbsViewForm.setLoginUrl(bbsViewForm.getRequestUrl());
		}
	}
}