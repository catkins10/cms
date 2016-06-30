package com.yuanluesoft.job.apply.actions.apply;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.job.apply.forms.Apply;

/**
 * 
 * @author linchuan
 *
 */
public class ChangeApplyStatus extends ApplyAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(forward!=null && "result".equals(forward.getName())) {
    		Apply applyForm = (Apply)form;
    		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
    		response.sendRedirect(request.getContextPath() + "/job/apply/apply.shtml?id=" + applyForm.getId() + (siteId==0 ? "" : "&siteId=" + siteId) + "&seq=" + UUIDLongGenerator.generateId());
    		return null;
    	}
    	return forward;
    }
}