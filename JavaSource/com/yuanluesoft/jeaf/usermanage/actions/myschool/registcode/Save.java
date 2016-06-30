package com.yuanluesoft.jeaf.usermanage.actions.myschool.registcode;

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
public class Save extends SchoolRegistCodeAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) {
    		throw new Exception();
    	}
        return executeSaveAction(mapping, form, request, response, false, null, "提交成功！请您及时通知尚未注册的老师。", null);
    }
}