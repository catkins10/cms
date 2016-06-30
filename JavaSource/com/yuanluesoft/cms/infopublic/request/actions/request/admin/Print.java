package com.yuanluesoft.cms.infopublic.request.actions.request.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;

/**
 * 
 * @author linchuan
 *
 */
public class Print extends RequestAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeLoadAction(mapping, form, request, response);
        if(forward==null || !"load".equals(forward.getName())) {
        	return forward;
        }
        PageService pageService = (PageService)getService("publicServicePrintPageService");
        pageService.writePage("cms/infopublic/request", "requestPrint", request, response, false);
        return null;
    }
}