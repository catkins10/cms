package com.yuanluesoft.jeaf.opinionmanage.actions.opiniontype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends OpinionTypeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeDeleteAction(mapping, form, request, response, null, null);
    	if(forward!=null && "result".equals(forward.getName())) {
    		return closeDialogAndRefreshOpener(request, response);
    	}
    	return forward;
    }
}