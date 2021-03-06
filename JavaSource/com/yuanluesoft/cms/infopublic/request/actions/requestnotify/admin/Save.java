package com.yuanluesoft.cms.infopublic.request.actions.requestnotify.admin;

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
public class Save extends RequestNotifyAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveComponentAction(mapping, form, "requestNotify", "notify", "requestId", "refreshRequest", false, request, response);
    }
}