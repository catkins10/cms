package com.yuanluesoft.bidding.enterprise.actions.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;

/**
 * 
 * @author yuanlue
 *
 */
public class Load extends com.yuanluesoft.jeaf.sso.actions.login.Load {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward actionForward = super.execute(mapping, form, request, response);
    	if(actionForward!=null && "load".equals(actionForward.getName())) {
    		//加载页面
        	PageService pageService = (PageService)getService("pageService");
        	pageService.writePage("bidding/enterprise", "login", request, response, false);
    		return null;
    	}
    	return actionForward;
    }
}