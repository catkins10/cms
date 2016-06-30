package com.yuanluesoft.portal.server.actions.portalpage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.portal.server.forms.PortalPage;
import com.yuanluesoft.portal.server.service.PortalService;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends PortalPageAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeSubmitAction(mapping, form, request, response, false, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#FormUtils.submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(com.yuanluesoft.jeaf.form.ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortalPage portalPageForm = (PortalPage)form;
		PortalService portalService = (PortalService)getService("portalService");
		portalService.deletePortalPage(portalPageForm.getApplicationName(), portalPageForm.getPageName(), portalPageForm.getUserId(), portalPageForm.getSiteId(), portalPageForm.getId(), sessionInfo);
	}
}