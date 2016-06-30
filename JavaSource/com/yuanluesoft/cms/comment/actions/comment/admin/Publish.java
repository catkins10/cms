package com.yuanluesoft.cms.comment.actions.comment.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 发布
 * @author yuanluesoft
 *
 */
public class Publish extends CommentAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executePublishAction(mapping, form, request, response);
    }
}