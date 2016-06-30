package com.yuanluesoft.jeaf.application.action.applicationnavigator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.application.forms.ListApplicationNavigatorTreeNodes;
import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class ListTreeNodes extends BaseAction {
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ListApplicationNavigatorTreeNodes treeNodeListForm = (ListApplicationNavigatorTreeNodes)form;
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		treeNodeListForm.setActionResult("NOSESSIONINFO");
    		return mapping.findForward("load");
        }
        ApplicationNavigatorService applicationNavigatorService = (ApplicationNavigatorService)getService("applicationNavigatorService");
        treeNodeListForm.setChildNodes(applicationNavigatorService.listApplicationNavigatorTreeNodes(treeNodeListForm.getApplicationName(), treeNodeListForm.getParentNodeId(), sessionInfo));
        treeNodeListForm.setActionResult("SUCCESS");
        return mapping.findForward("load");
    }
}