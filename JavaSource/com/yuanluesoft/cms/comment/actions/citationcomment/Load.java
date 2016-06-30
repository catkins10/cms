package com.yuanluesoft.cms.comment.actions.citationcomment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.comment.pages.CitationCommentPageService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	CitationCommentPageService pageService = (CitationCommentPageService)getService("citationCommentPageService");
    	pageService.writePage(request.getParameter("applicationName"), request.getParameter("pageName"), request, response, false);
        return null;
    }
}