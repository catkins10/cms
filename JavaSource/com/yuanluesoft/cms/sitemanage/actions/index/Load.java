package com.yuanluesoft.cms.sitemanage.actions.index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 打开站点/栏目首页
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
    	SiteService siteService = (SiteService)getService("siteService");
    	WebDirectory webDirectory = (WebDirectory)siteService.getDirectory(RequestUtils.getParameterLongValue(request, "siteId"));
    	if(webDirectory!=null && webDirectory.getRedirectUrl()!=null && !webDirectory.getRedirectUrl().isEmpty()) {
    		response.sendRedirect(webDirectory.getRedirectUrl());
			return null;
		}
    	request.setAttribute(PageService.PAGE_ATTRIBUTE_RECORD, webDirectory);
    	PageService pageService = (PageService)getService("siteIndexPageService");
    	pageService.writePage("cms/sitemanage", "index", request, response, false);
    	return null;
    }
}