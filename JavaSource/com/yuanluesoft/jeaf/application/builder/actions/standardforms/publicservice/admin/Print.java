package com.yuanluesoft.jeaf.application.builder.actions.standardforms.publicservice.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;

/**
 * 
 * @author linchuan
 *
 */
public class Print extends PublicServiceFormAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = executeLoadAction(mapping, form, request, response);
        if(forward==null || !"load".equals(forward.getName())) {
        	return forward;
        }
        PublicServiceAdminForm publicServiceAdminForm = (PublicServiceAdminForm)form;
        String formName = form.getClass().getSimpleName();
        PageService pageService = (PageService)getService("publicServicePrintPageService");
        pageService.writePage(publicServiceAdminForm.getFormDefine().getApplicationName(), formName.substring(0, 1).toLowerCase() + formName.substring(1) + "Print", request, response, false);
        return null;
    }
}