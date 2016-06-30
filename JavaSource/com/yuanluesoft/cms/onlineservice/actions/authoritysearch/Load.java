package com.yuanluesoft.cms.onlineservice.actions.authoritysearch;

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
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PageService pageService = (PageService)getService("authorityDirectoryPageService");
    	String pageName = request.getParameter("pageName");
    	pageService.writePage("cms/onlineservice", pageName==null || pageName.isEmpty() ? "authoritySearch" : pageName, request, response, false);
    	return null;
    }
}