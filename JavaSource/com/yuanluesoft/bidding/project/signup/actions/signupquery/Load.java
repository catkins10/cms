package com.yuanluesoft.bidding.project.signup.actions.signupquery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.base.BiddingBaseAction;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 投标报名查询
 * @author linchuan
 *
 */
public class Load extends BiddingBaseAction {
	
    /*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if("receivePaperDocuments".equals(request.getParameter("queryFor"))) { //查询的目的是领取纸质标书和图纸
			//会话校验
			try {
				getSessionInfo(request, response);
			}
			catch(SessionException se) {
				return redirectToLogin(this, mapping, form, request, response, se, false);
			}
		}
		String signUpNo = request.getParameter("signUpNo");
		if(signUpNo!=null && !signUpNo.equals("")) {
			ValidateCodeService validateCodeService = (ValidateCodeService)getService("validateCodeService");
    		//检查校验码
    		if(!validateCodeService.validateCode(request.getParameter("validateCode"), request)) {
				request.setAttribute("error", "验证码错误");
			}
    		else {
    			validateCodeService.cleanCode(request, response); //清除校验码
				//按报名号获取报名记录
				BiddingService biddingService = (BiddingService)getService("biddingService");
				BiddingSignUp signUp = biddingService.loadSignUp(signUpNo, false);
				if(signUp!=null) { //有报名记录
					//重定向到查询结果页面
					response.sendRedirect("signUpQueryResult.shtml?signUpNo=" + signUpNo + "&projectId=" + signUp.getProjectId() + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
					return null;
				}
				else {
					request.setAttribute("error", "报名号不存在");
				}
			}
		}
		//加载页面
    	PageService pageService = (PageService)getService("biddingSignUpQueryPageService");
    	String pageName = request.getParameter("pageName");
    	pageService.writePage("bidding/project/signup", (pageName==null ? "biddingSignUpQuery" : pageName), request, response, false);
		return null;
    }
}