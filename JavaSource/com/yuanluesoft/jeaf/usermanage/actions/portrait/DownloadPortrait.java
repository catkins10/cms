package com.yuanluesoft.jeaf.usermanage.actions.portrait;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class DownloadPortrait extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	com.yuanluesoft.jeaf.usermanage.forms.Portrait portraitForm = (com.yuanluesoft.jeaf.usermanage.forms.Portrait)form;
    	((PersonService)getService("personService")).downloadPersonPortrait(portraitForm.getPersonId(), request, response);
        return null;
    }
}