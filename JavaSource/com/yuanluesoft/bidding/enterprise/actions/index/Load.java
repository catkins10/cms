package com.yuanluesoft.bidding.enterprise.actions.index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.base.BiddingBaseAction;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 企业用户首页
 * @author yuanlue
 *
 */
public class Load extends BiddingBaseAction {
	
    /*
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	//加载页面
    	PageService pageService = (PageService)getService("biddingEnterpriseIndexPageService");
    	pageService.writePage("bidding/enterprise", "index", request, response, false);
		return null;
    }
}