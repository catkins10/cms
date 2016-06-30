package com.yuanluesoft.portal.server.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.portal.server.forms.PortalForm;

/**
 * 
 * @author linchuan
 *
 */
public class PortalDialogFormAction extends DialogFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		PortalForm portalForm = (PortalForm)form;
		if(portalForm.getUserId()==sessionInfo.getUserId()) { //用户自己的页面
			return;
		}
		if(portalForm.getSiteId()!=-1) { //站点
			 if(!((SiteService)getService("siteService")).checkParentDirectoryPopedom(portalForm.getSiteId(), "manager", sessionInfo)) { //检查用户对站点的管理权限
				 throw new PrivilegeException();
			 }
 		}
		else {
			if(!getOrgService().checkPopedom(portalForm.getUserId(), "manager", sessionInfo)) { //检查用户对组织机构的管理员权限
				throw new PrivilegeException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#executeLoadAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeLoadAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortalForm portalForm = (PortalForm)form;
		externalAction = portalForm.getSiteId()>=0; //是否对外操作
		return super.executeLoadAction(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#executeSubmitAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean, java.lang.String, java.lang.String)
	 */
	public ActionForward executeSubmitAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String actionResult, String forwardName) throws Exception {
		PortalForm portalForm = (PortalForm)form;
		externalAction = portalForm.getSiteId()>=0; //是否对外操作
		return super.executeSubmitAction(mapping, form, request, response, reload, actionResult, forwardName);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#generateScriptAfterSubmit()
	 */
	protected String generateScriptAfterSubmit() {
		return "DialogUtils.getDialogOpener().location.reload();";
	}
}