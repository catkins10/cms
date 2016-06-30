package com.yuanluesoft.bidding.project.signup.actions.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Complete extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean paymentSuccess = "true".equals(request.getParameter("paymentSuccess"));
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
    	if(paymentSuccess) {
	    	BiddingService biddingService = (BiddingService)getService("biddingService");
	    	BiddingSignUp signUp = biddingService.loadSignUp(request.getParameter("signUpNo"), true);
	    	if(signUp.getPaymentTime()!=null || signUp.getDrawPaymentTime()!=null || signUp.getPledgePaymentTime()!=null) { //缴费成功
	    		//重定向到报名查询
	    		response.sendRedirect("signUpQueryResult.shtml?signUpNo=" + signUp.getSignUpNo() + (siteId==0 ? "" : "&siteId=" + siteId));
	    		return null;
	    	}
	    }
		return mapping.findForward("paymentFailed");
    }
}