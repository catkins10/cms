package com.yuanluesoft.bidding.project.actions.search;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.base.BiddingBaseAction;
import com.yuanluesoft.cms.pagebuilder.PageService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BiddingBaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String type = request.getParameter("type");
    	//加载页面
    	PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("bidding/project", type, request, response, false);
		return null;
    }
}