package com.yuanluesoft.bidding.project.signup.actions.signupquery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.base.BiddingBaseAction;
import com.yuanluesoft.cms.pagebuilder.PageService;

/**
 * 报名号查询结果
 * @author linchuan
 *
 */
public class Result extends BiddingBaseAction {
	
    /*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//加载页面
    	PageService pageService = (PageService)getService("biddingSignUpQueryResultPageService");
    	pageService.writePage("bidding/project/signup", "biddingSignUpQueryResult", request, response, false);
		return null;
    }
}