package com.yuanluesoft.portal.server.actions.portletjs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.portal.server.actions.PortalDialogFormAction;
import com.yuanluesoft.portal.server.forms.PortletJs;
import com.yuanluesoft.portal.server.service.PortalService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends PortalDialogFormAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript"); //设置输出内容为脚本
		executeLoadAction(mapping, form, request, response);
		return null;
	}
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(com.yuanluesoft.jeaf.form.ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		PortletJs portletJsForm = (PortletJs)form;
		PortalService portalService = (PortalService)getService("portalService");
		if("move".equals(portletJsForm.getPortletAction())) { //移动PORTLET
			portalService.movePortlet(portletJsForm.getApplicationName(), portletJsForm.getPageName(), portletJsForm.getUserId(), portletJsForm.getSiteId(), portletJsForm.getPageId(), portletJsForm.getPortletInstanceId(), portletJsForm.getPortletColumnIndex(), portletJsForm.getPortletIndex(), sessionInfo);
    	}
    	else if("minimize".equals(portletJsForm.getPortletAction())) { //最小化PORTLET
    		portalService.minimizePortlet(portletJsForm.getApplicationName(), portletJsForm.getPageName(), portletJsForm.getUserId(), portletJsForm.getSiteId(), portletJsForm.getPageId(), portletJsForm.getPortletInstanceId(), sessionInfo);
    	}
    	else if("restore".equals(portletJsForm.getPortletAction())) { //还原PORTLET
    		portalService.restorePortlet(portletJsForm.getApplicationName(), portletJsForm.getPageName(), portletJsForm.getUserId(), portletJsForm.getSiteId(), portletJsForm.getPageId(), portletJsForm.getPortletInstanceId(), sessionInfo);
    	}
    	else if("remove".equals(portletJsForm.getPortletAction())) { //移除PORTLET
    		portalService.removePortlet(portletJsForm.getApplicationName(), portletJsForm.getPageName(), portletJsForm.getUserId(), portletJsForm.getSiteId(), portletJsForm.getPageId(), portletJsForm.getPortletInstanceId(), sessionInfo);
    	}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#transactException(java.lang.Exception, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean)
	 */
	protected ActionForward transactException(Exception e, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean resumeAction) throws Exception {
		if((e instanceof SessionException) || (e instanceof PrivilegeException)) {
			response.getWriter().write("window.location.reload();");
		}
		else {
			Logger.exception(e);
			response.getWriter().write("alert('服务器错误，请联系系统管理员。');");
		}
		return null;
	}
}