package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author yuanluesoft
 *
 */
public class SelectAttachment extends ServiceItemAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeAttachmentAction(mapping, form, request, response);
    }
}