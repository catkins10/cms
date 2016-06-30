package com.yuanluesoft.jeaf.usermanage.actions.registstudent;

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
public class Regist extends RegistStudentAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) {
    		throw new Exception();
    	}
        return executeSaveAction(mapping, form, request, response, false, null, "注册成功！您的帐号将在班主任审核后生效！", null);
    }
}