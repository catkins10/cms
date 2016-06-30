package com.yuanluesoft.fdi.customer.actions.contact.admin.discuss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fdi.customer.forms.admin.Discuss;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends DiscussAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Discuss discussForm = (Discuss)form;
    	return executeDeleteComponentAction(mapping, form, "discuss", "contacts", (discussForm.isOpenFromContact() ? "refreshContact" : "refreshCompany"), request, response);
    }
}