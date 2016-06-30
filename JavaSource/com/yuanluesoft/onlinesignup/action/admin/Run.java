package com.yuanluesoft.onlinesignup.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.onlinesignup.forms.admin.SignUpForm;

/**
 * 
 * 
 * @author zyh
 *
 */
public class Run extends SignUpAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SignUpForm signUpForm = (SignUpForm)form ;
    	signUpForm.setStatus(4);
    	return executeRunAction(mapping, form, request, response, false, null, null);
    }
}