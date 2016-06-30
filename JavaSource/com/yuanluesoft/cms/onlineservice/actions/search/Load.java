package com.yuanluesoft.cms.onlineservice.actions.search;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
	/*
	 *  (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String searchContent = request.getParameter("searchContent");
    	PageService pageService = (PageService)getService("pageService");
    	String applicationName = "cms/onlineservice";
    	String pageName;
    	if("表格下载".equals(searchContent)) {
    		pageName = "downloadSearch";
    	}
    	else if("样表下载".equals(searchContent)) {
    		pageName = "exampleSearch";
    	}
    	else if("常见问题".equals(searchContent)) {
    		applicationName = "cms/onlineservice/faq";
    		pageName = "search";
    	}
    	else {
    		pageName = "search";
    	}
    	pageService.writePage(applicationName, pageName, request, response, false);
    	return null;
    }
}