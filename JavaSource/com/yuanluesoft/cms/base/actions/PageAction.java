package com.yuanluesoft.cms.base.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class PageAction extends BaseAction {
	
	public PageAction() {
		super();
		 externalAction = true; //对外的操作
	}

	/**
	 * 执行加载页面的操作
	 * @param applicationName
	 * @param pageName
	 * @param pageServiceName
	 * @param anonymousPage
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeLoadPageAction(String applicationName, String pageName, String pageServiceName, boolean anonymousPage, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!anonymousPage) { //不是匿名访问的页面
			try {
	        	getSessionInfo(request, response);
	        }
	        catch(SessionException se) {
	        	return redirectToLogin(this, mapping, form, request, response, se, false);
	        }
        }
        PageService pageService = (PageService)getService(pageServiceName==null ? "pageService" : pageServiceName);
        pageService.writePage(applicationName, pageName, request, response, false);
        return null;
    }
}