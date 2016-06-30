package com.yuanluesoft.cms.templatemanage.actions.customcssfile;

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
public class Delete extends CustomCssFileAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeDeleteAction(mapping, form, request, response, null, null);
    	if(forward!=null && "result".equals(forward.getName())) {
    		refreshDialogOpener(request, response);
            return null;
    	}
    	return forward;
    }
}