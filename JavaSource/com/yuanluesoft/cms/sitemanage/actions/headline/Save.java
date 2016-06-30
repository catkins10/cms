package com.yuanluesoft.cms.sitemanage.actions.headline;

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
public class Save extends HeadlineAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ActionForward forward = executeSaveAction(mapping, form, request, response, false, null, null, null);
    	if(forward!=null && "result".equals(forward.getName())) {
	    	//关闭对话框
	    	String html = "<html>" +
						  " <head>" +
						  "  <script language=\"JavaScript\" charset=\"utf-8\" src=\"" + request.getContextPath() + "/jeaf/common/js/common.js\"></script>" +
						  " </head>" +
						  " <body onload=\"DialogUtils.closeDialog()\"></body>" +
						  "</html>";
			response.getWriter().write(html);
			return null;
    	}
    	return forward;
    }
}