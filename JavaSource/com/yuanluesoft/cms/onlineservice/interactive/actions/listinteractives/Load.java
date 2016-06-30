package com.yuanluesoft.cms.onlineservice.interactive.actions.listinteractives;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String listType = RequestUtils.getParameterStringValue(request, "type");
    	if(listType==null) {
    		return null;
    	}
    	if(listType.indexOf("My")!=-1) { //获取个人的记录列表
	    	try {
	    		getSessionInfo(request, response);
	    	}
	    	catch(SessionException se) {
	    		return redirectToLogin(this, mapping, form, request, response, se, false);
	    	}
    	}
    	//加载页面
    	PageService pageService = (PageService)getService("pageService");
    	pageService.writePage(RequestUtils.getParameterStringValue(request, "applicationName"), listType, request, response, false);
		return null;
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL(javax.servlet.http.HttpServletRequest)
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		return new Link(Environment.getWebApplicationSafeUrl() +  "/jeaf/sso/login.shtml?external=true&templateName=onlineservice" + (siteId==0 ? "" : "&siteId=" + siteId), "utf-8");
	}
}