package com.yuanluesoft.bidding.project.signup.actions.payment;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.base.BiddingBaseAction;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BiddingBaseAction {
	
    /*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		externalAction = true;
		BiddingService biddingService = (BiddingService)getService("biddingService");
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		String signUpNo = request.getParameter("signUpNo");
		BiddingSignUp signUp = biddingService.loadSignUp(signUpNo, false);
		if(!biddingProjectService.isRealNameSignUp(signUp.getProjectId())) { //不是实名报名
			anonymousEnable = true;
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
		}
    	biddingService.redirectToPayment(signUpNo, request.getParameter("type"), RequestUtils.getParameterLongValue(request, "siteId"), response);
    	return null;
    }
}