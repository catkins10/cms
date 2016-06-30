package com.yuanluesoft.cms.siteresource.actions.admin.resourceview.modifyreaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.actions.admin.resourceview.ResourceViewAction;
import com.yuanluesoft.cms.siteresource.forms.ResourceView;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class Submit extends ResourceViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ResourceView resourceViewForm = (ResourceView)viewForm;
		SiteService siteService = (SiteService)getService("siteService");
		if(!siteService.checkPopedom(resourceViewForm.getSiteId(), "manager", sessionInfo)) {
			throw new PrivilegeException();
		}
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		siteResourceService.modifyReaders(viewForm.getViewPackage().getView(), viewForm.getViewPackage().getCategories(), viewForm.getViewPackage().getSearchConditions(), viewForm.getViewPackage().getSelectedIds(), request.getParameter("modifyMode"), "true".equals(request.getParameter("selectedResourceOnly")), "true".equals(request.getParameter("deleteNotColumnVisitor")), request.getParameter("readerIds"), request, sessionInfo);
		viewForm.getViewPackage().setSelectedIds(null);
		return mapping.getInputForward();
	}
}