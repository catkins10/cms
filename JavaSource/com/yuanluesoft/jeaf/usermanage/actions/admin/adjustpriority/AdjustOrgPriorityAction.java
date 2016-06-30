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
public class AdjustOrgPriorityAction extends AdjustPriorityAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewApplicationName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewApplicationName(AdjustPriority adjustPriorityForm) {
		return "jeaf/usermanage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewName(AdjustPriority adjustPriorityForm) {
		return "org";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#resetView(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void resetView(AdjustPriority adjustPriorityForm, View view, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.resetView(adjustPriorityForm, view, request, sessionInfo);
		long orgId = Long.parseLong(request.getParameter("orgId"));
		String where = "Org.parentDirectoryId=" + orgId +
					   (orgId==0 ? " and Org.id!=0" : "");
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
