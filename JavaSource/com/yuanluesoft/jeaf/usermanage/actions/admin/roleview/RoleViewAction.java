package com.yuanluesoft.jeaf.usermanage.actions.admin.roleview;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.forms.admin.RoleView;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author yuanluesoft
 *
 */
public class RoleViewAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "jeaf/usermanage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "role";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		RoleView roleViewForm = (RoleView)viewForm;
		//设置查询条件
		String where = "Role.orgId=" + roleViewForm.getOrgId();
		view.addWhere(where);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.initForm(viewForm, request, sessionInfo);
		//检查用户对组织机构的完全控制权限
		RoleView roleViewForm = (RoleView)viewForm;
		OrgService orgService = (OrgService)getService("orgService");
	    if(!orgService.checkPopedom(roleViewForm.getOrgId(), "manager", sessionInfo)) {
	    	throw new PrivilegeException();
	    }
	}
}