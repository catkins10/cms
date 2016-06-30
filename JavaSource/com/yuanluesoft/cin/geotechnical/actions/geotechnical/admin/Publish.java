package com.yuanluesoft.cin.geotechnical.actions.geotechnical.admin;

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
public class Publish extends GeotechnicalAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executePublishAction(mapping, form, request, response);
    }
}