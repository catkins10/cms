package com.yuanluesoft.jeaf.sso.actions.forgetpassword;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sso.forms.ForgetPassword;

/**
 * 
 * @author chuan
 *
 */
public class Reset extends ForgetPasswordAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ForgetPassword forgetPasswordForm = (ForgetPassword)form;
    	return executeSubmitAction(mapping, form, request, response, "inputLoginName".equals(forgetPasswordForm.getAct()), null, null);
    }
}