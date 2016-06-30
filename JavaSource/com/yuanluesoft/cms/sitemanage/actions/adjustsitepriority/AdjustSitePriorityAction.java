package com.yuanluesoft.cms.sitemanage.actions.adjustsitepriority;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction;
import com.yuanluesoft.jeaf.dialog.forms.AdjustPriority;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class AdjustSitePriorityAction extends AdjustPriorityAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewApplicationName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewApplicationName(AdjustPriority adjustPriorityForm) {
		return "cms/sitemanage";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#getViewName(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority)
	 */
	public String getViewName(AdjustPriority adjustPriorityForm) {
		return "site";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#resetView(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void resetView(AdjustPriority adjustPriorityForm, View view, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		long siteId = RequestUtils.getParameterLongValue(request, "parentSiteId");
		String where = "WebDirectory.parentDirectoryId=" + siteId +
					   (siteId==0 ? " and WebDirectory.id!=0" : "");
		view.addWhere(where);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.adjustpriority.AdjustPriorityAction#checkAdjustPrivilege(com.yuanluesoft.jeaf.dialog.forms.AdjustPriority, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkAdjustPrivilege(AdjustPriority form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		SiteService siteService = (SiteService)getService("siteService");
		//获取用户对站点/栏目的权限
	   	if(siteService.checkPopedom(RequestUtils.getParameterLongValue(request, "parentSiteId"), "manager", sessionInfo)) {
			return;
		}
	    throw new PrivilegeException();
	}
}