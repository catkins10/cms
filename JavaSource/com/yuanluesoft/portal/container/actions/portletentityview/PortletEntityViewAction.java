package com.yuanluesoft.portal.container.actions.portletentityview;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.portal.container.forms.PortletEntityView;

/**
 * 
 * @author linchuan
 *
 */
public class PortletEntityViewAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplicationName(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "portal";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "portletEntities";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#checkPrivilege(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkPrivilege(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws PrivilegeException {
		try {
			PortletEntityView portletEntityView = (PortletEntityView)viewForm;
			if(portletEntityView.getSiteId()>=0) { //站点
				//检查用户的站点管理权限
				SiteService siteService = (SiteService)getService("siteService");
				if(!siteService.checkPopedom(portletEntityView.getSiteId(), "manager", sessionInfo)) {
					throw new PrivilegeException();
				}
			}
			else { //组织机构
				//检查用户的组织机构管理权限
				if(!getOrgService().checkPopedom(portletEntityView.getOrgId(), "manager", sessionInfo)) {
					throw new PrivilegeException();
				}
			}
		}
		catch (SystemUnregistException e) {
			throw new PrivilegeException(e);
		}
	}
}