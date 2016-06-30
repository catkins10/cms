package com.yuanluesoft.cms.publicservice.actions.modifyreaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.application.forms.ApplicationView;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class Submit extends ApplicationViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ApplicationView applicationViewForm = (ApplicationView)viewForm;
		if(!getAcl(applicationViewForm.getApplicationName(), sessionInfo).contains(AccessControlService.ACL_APPLICATION_MANAGER)) {
			throw new PrivilegeException();
		}
		PublicService publicService = (PublicService)getService("publicService");
		publicService.modifyReaders(viewForm.getViewPackage().getView(), viewForm.getViewPackage().getCategories(), viewForm.getViewPackage().getSearchConditions(), viewForm.getViewPackage().getSelectedIds(), request.getParameter("modifyMode"), "true".equals(request.getParameter("selectedOnly")), request.getParameter("readerIds"), request, sessionInfo);
		viewForm.getViewPackage().setSelectedIds(null);
		return mapping.getInputForward();
	}
}