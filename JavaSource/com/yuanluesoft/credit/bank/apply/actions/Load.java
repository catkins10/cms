package com.yuanluesoft.credit.bank.apply.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author ZYH
 *
 */
public class Load extends ApplyAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			getSessionInfo(request, response);
		}
		catch(SessionException se) {
			//会话异常,重定向到登录界面
				return redirectToLogin(this, mapping, form, request, response, se, true);
			}
    	return executeLoadAction(mapping, form, request, response);
    }
}