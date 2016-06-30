package com.yuanluesoft.bidding.project.signup.actions.signup;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 
 * @author linchuan
 *
 */
public class Complete extends BaseAction {
	
    /*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long projectId = RequestUtils.getParameterLongValue(request, "projectId");
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		SessionInfo sessionInfo = null;
		if(biddingProjectService.isRealNameSignUp(projectId)) { //实名报名
			try {
				sessionInfo = getSessionInfo(request, response);
			}
	    	catch(SessionException se) {
	    		return redirectToLogin(this, mapping, form, request, response, se, true);
	    	}
	    	EnterpriseService enterpriseService = (EnterpriseService)getService("enterpriseService");
			if(!enterpriseService.isEnterpriseValid(sessionInfo.getDepartmentId())) { //企业未通过审核
				throw new Exception("企业未通过审核,不允许报名");
			}
		}
		else {
			anonymousEnable = true;
			try {
				sessionInfo = getSessionInfo(request, response);
	    	}
	    	catch(SessionException se) {
	    		if(SessionException.SESSION_TIMEOUT.equals(se.getMessage())) {
	        		//重定向到提醒用户注销页面
	    			response.sendRedirect("signUpAnonymous.shtml?redirect=" + URLEncoder.encode(Environment.getWebApplicationUrl() + "/bidding/project/signup/signUp.shtml?projectId=" + projectId, "utf-8") + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
	        		return null;
	    		}
	    		sessionInfo = null;
	    	}
	    	if(sessionInfo!=null && !SessionService.ANONYMOUS.equals(sessionInfo.getLoginName())) { //不是匿名用户
	    		//重定向到提醒用户注销页面
	    		response.sendRedirect("signUpAnonymous.shtml?redirect=" + URLEncoder.encode(Environment.getWebApplicationUrl() + "/bidding/project/signup/signUp.shtml?projectId=" + projectId, "utf-8") + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
	    		return null;
	    	}
		}
		//检查校验码
		ValidateCodeService validateCodeService = (ValidateCodeService)getService("validateCodeService");
		if(!validateCodeService.validateCode(request.getParameter("validateCode"), request)) {
			//重定向到报名页面
			response.sendRedirect("signUp.shtml?projectId=" + projectId + "&error=" + URLEncoder.encode("验证码错误", "utf-8") + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
    		return null;
		}
		validateCodeService.cleanCode(request, response); //清除校验码
		//完成报名
    	BiddingService biddingService = (BiddingService)getService("biddingService");
    	BiddingSignUp signUp;
    	try {
    		signUp = biddingService.completeSignUp(projectId, request, response, sessionInfo);
    	}
    	catch(ServiceException se) {
    		Logger.exception(se);
			//重定向到报名页面
    		response.sendRedirect("signUp.shtml?projectId=" + projectId + "&error=" + URLEncoder.encode(se.getMessage(), "utf-8") + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
    		return null;
    	}
    	//重定向到报名查询结果页面
    	response.sendRedirect("signUpQueryResult.shtml?signUpNo=" + signUp.getSignUpNo() + "&projectId=" + signUp.getProjectId() + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
		return null;
    }
}