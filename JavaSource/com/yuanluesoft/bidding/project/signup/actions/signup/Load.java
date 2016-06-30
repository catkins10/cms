package com.yuanluesoft.bidding.project.signup.actions.signup;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.base.BiddingBaseAction;
import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 投标报名
 * @author linchuan
 *
 */
public class Load extends BiddingBaseAction {
	
    /*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//检查项目是否强制实名报名
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		long projectId = RequestUtils.getParameterLongValue(request, "projectId");
		if(biddingProjectService.isRealNameSignUp(projectId)) { //实名报名
			BiddingSessionInfo sessionInfo;
			try {
				sessionInfo = (BiddingSessionInfo)getSessionInfo(request, response);
			}
	    	catch(SessionException se) {
	    		return redirectToLogin(this, mapping, form, request, response, se, false);
	    	}
	    	EnterpriseService enterpriseService = (EnterpriseService)getService("enterpriseService");
			if(!enterpriseService.isEnterpriseValid(sessionInfo.getDepartmentId())) { //企业未通过审核
				response.sendRedirect("enterpriseInvalidPrompt.shtml?siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
	    		return null;
			}
        	//检查是否已经报过
	    	BiddingService biddingService = (BiddingService)getService("biddingService");
	    	BiddingSignUp signUp = biddingService.loadSignUpByEnterprise(sessionInfo.getDepartmentId(), projectId, false);
	    	if(signUp!=null) {
	        	//重定向到报名查询结果页面
	    		response.sendRedirect("signUpQueryResult.shtml?signUpNo=" + signUp.getSignUpNo() + "&projectId=" + signUp.getProjectId() + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
	    		return null;
	    	}
	    	//加载页面
	    	PageService pageService = (PageService)getService("biddingSignUpPageService");
	    	pageService.writePage("bidding/project/signup", "realNameBiddingSignUp", request, response, false);
		}
		else { //匿名报名
			anonymousEnable = true;
			externalAction = true;
			SessionInfo sessionInfo = null;
			try {
				sessionInfo = getSessionInfo(request, response);
	    	}
	    	catch(SessionException se) {
	    		//没有会话,执行sso注销
	    		return redirectToLogout(mapping, form, RequestUtils.getRequestURL(request, true), request, response);
	    	}
	    	//检查是否匿名用户
	    	if(!SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //不是匿名用户
	    		//重定向提醒用户注销页面
	    		response.sendRedirect("signUpAnonymous.shtml?redirect=" + URLEncoder.encode(RequestUtils.getRequestURL(request, true), "utf-8"));
	    		return null;
	    	}
	    	//加载页面
	    	PageService pageService = (PageService)getService("biddingSignUpPageService");
	    	pageService.writePage("bidding/project/signup", "biddingSignUp", request, response, false);
		}
		return null;
    }
}