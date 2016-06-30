package com.yuanluesoft.cms.templatemanage.actions.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.templatemanage.forms.Template;

/**
 * 
 * @author linchuan
 *
 */
public class Save extends TemplateAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Template templateForm = (Template)form;
    	return executeSaveAction(mapping, form, request, response, templateForm.getTemplateAction()!=null, null, null, null);
    }
}