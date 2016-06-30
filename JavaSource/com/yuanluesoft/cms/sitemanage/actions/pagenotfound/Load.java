package com.yuanluesoft.cms.sitemanage.actions.pagenotfound;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 页面找不到提示
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
	/*
	 *  (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("cms/sitemanage", "pageNotFoundPrompt", request, response, false);
    	return null;
    }
}