package com.yuanluesoft.lss.insurance.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.lss.insurance.model.InsuranceSessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class InsurancePageAction extends BaseAction {
	protected String pageName; //页面名称

	public InsurancePageAction() {
		super();
		externalAction = true;
		sessionInfoClass = InsuranceSessionInfo.class;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		PageService pageService = (PageService)getService("pageService");
		pageService.writePage("lss/insurance", pageName, request, response, false);
		return null;
	}
}