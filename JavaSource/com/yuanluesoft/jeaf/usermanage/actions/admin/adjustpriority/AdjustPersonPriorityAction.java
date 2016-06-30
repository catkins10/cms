package com.yuanluesoft.jeaf.usermanage.actions.admin.adjustpriority;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction;
import com.yuanluesoft.jeaf.dialog.forms.AdjustPriority;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class AdjustPersonPriorityAction extends AdjustPriorityAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.admin.adjustpriority.AdjustOrgPriorityAction#getViewApplicationName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewApplicationName(AdjustPriority adjustPriorityForm) {
		return "jeaf/usermanage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.admin.adjustpriority.AdjustOrgPriorityAction#getViewName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewName(AdjustPriority adjustPriorityForm) {
		return "person";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.admin.adjustpriority.AdjustOrgPriorityAction#resetView(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void resetView(AdjustPriority adjustPriorityForm, View view, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.resetView(adjustPriorityForm, view, request, sessionInfo);
		long orgId = Long.parseLong(request.getParameter("orgId"));
		//获取属于当前机构及其下级机构的人员
		view.addJoin(", OrgSubjection OrgSubjection");
		String where = "subjections.orgId=OrgSubjection.directoryId " +
					   " and OrgSubjection.parentDirectoryId=" +  orgId;
		view.addWhere(where);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#checkAdjustPrivilege(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkAdjustPrivilege(AdjustPriority form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		OrgService orgService = (OrgService)getService("orgService");
		long orgId = Long.parseLong(request.getParameter("orgId"));
		//获取用户对站点/栏目的权限
	   	if(orgService.checkPopedom(orgId, "manager", sessionInfo)) {
			return;
		}
		throw new PrivilegeException();
	}
}