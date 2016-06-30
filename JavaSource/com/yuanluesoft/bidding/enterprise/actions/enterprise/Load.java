package com.yuanluesoft.bidding.enterprise.actions.enterprise;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends EnterpriseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	/*try {
    		getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}*/
    	//加载页面
    	//PageService pageService = (PageService)getService("biddingEnterprisePageService");
    	//pageService.writePage("bidding/enterprise", "enterprise", request, response, false);
		return executeLoadAction(mapping, form, request, response);
    }
}