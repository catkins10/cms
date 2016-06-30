package com.yuanluesoft.cms.templatemanage.actions.exporttemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * TODO: 权限控制
 * @author yuanluesoft
 *
 */
public class Export extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	TemplateService templateService = (TemplateService)getService("templateService");
		templateService.exportTemplate(request, response, Long.parseLong(request.getParameter("id")));
    	return null;
    }
}