package com.yuanluesoft.cms.complaint.actions.complaint.admin;

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
public class Print extends ComplaintAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeLoadAction(mapping, form, request, response);
        if(forward==null || !"load".equals(forward.getName())) {
        	return forward;
        }
        PageService pageService = (PageService)getService("publicServicePrintPageService");
        pageService.writePage("cms/complaint", "complaintPrint", request, response, false);
        return null;
    }
}