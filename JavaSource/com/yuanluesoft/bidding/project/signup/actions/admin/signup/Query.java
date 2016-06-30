package com.yuanluesoft.bidding.project.signup.actions.admin.signup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.signup.forms.admin.SignUp;

/**
 * 查询
 * @author linchuan
 *
 */
public class Query extends SignUpAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SignUp signUpForm = (SignUp)form;
    	signUpForm.setId(0);
    	executeLoadAction(mapping, form, request, response);
    	return mapping.getInputForward();
    }
}