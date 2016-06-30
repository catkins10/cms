package com.yuanluesoft.bidding.project.signup.actions.signupquery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.base.BiddingBaseAction;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ReceivePaperDocuments extends BiddingBaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	BiddingService biddingService = (BiddingService)getService("biddingService");
    	biddingService.receivePaperDocuments(request.getParameter("signUpNo"), sessionInfo);
    	//重定向到报名查询结果页面
    	response.sendRedirect("signUpQueryResult.shtml?signUpNo=" + request.getParameter("signUpNo") + "&projectId=" + request.getParameter("projectId") + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
    	return null;
    }
}