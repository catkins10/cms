package com.yuanluesoft.cms.infopublic.request.actions.personalrequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.request.forms.PersonalRequest;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends PersonalRequestAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeLoadAction(mapping, form, request, response);
    	//检查当前加载的是不是企业申请
    	PersonalRequest requestForm = (PersonalRequest)form;
    	if(requestForm.getProposerType()=='2' && (requestForm.getErrors()==null || requestForm.getErrors().isEmpty())) {
    		//重定向到企业申请页面
    		response.sendRedirect("companyRequest.shtml?" + request.getQueryString());
    		return null;
    	}
    	return forward;
    }
}