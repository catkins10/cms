package com.yuanluesoft.bidding.project.report.actions.signupview;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan;
import com.yuanluesoft.bidding.project.report.forms.admin.SignUpView;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class SignUpViewAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "bidding/project/report";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "signUp";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		SignUpView signUpView = (SignUpView)viewForm;
		//检查项目报名时间是否已经结束
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		BiddingProject project = biddingProjectService.getProject(signUpView.getProjectId());
		if(project.getPlans()==null) {
			throw new PrivilegeException();
		}
		BiddingProjectPlan plan = (BiddingProjectPlan)project.getPlans().iterator().next();
		if(plan.getBuyDocumentEnd()!=null && !DateTimeUtils.now().after(plan.getBuyDocumentEnd())) {
			throw new PrivilegeException();
		}
		view.addWhere("BiddingSignUp.projectId=" + signUpView.getProjectId());
	}
}