/*
 * Created on 2007-4-24
 *
 */
package com.yuanluesoft.jeaf.usermanage.actions.admin.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.usermanage.forms.admin.Person;

/**
 * 
 * @author linchuan
 *
 */
public class Save extends PersonAction {
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) {
    		throw new Exception();
    	}
    	Person personForm = (Person)form;
    	return executeSaveAction(mapping, form, request, response, false, null, personForm.getAct().equals(OPEN_MODE_CREATE) ? "注册成功" : "保存成功", null);
    }
}
