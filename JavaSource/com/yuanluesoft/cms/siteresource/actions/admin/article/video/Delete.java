package com.yuanluesoft.cms.siteresource.actions.admin.article.video;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author chuan
 *
 */
public class Delete extends VideoAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeDeleteComponentAction(mapping, form, "video", "videos", "refreshArticle", request, response);
    }
}