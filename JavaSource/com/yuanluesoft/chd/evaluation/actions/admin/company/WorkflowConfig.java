package com.yuanluesoft.chd.evaluation.actions.admin.company;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.chd.evaluation.forms.admin.Company;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowConfig extends CompanyAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(!"result".equals(forward.getName())) {
    		return forward;
    	}
    	workflowConfig((Company)form, request, response, RequestUtils.getSessionInfo(request));
    	return null;
    }
}