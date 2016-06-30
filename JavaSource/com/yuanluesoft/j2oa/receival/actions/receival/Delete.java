package com.yuanluesoft.j2oa.receival.actions.receival;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author LinChuan
 *
 */
public class Delete extends ReceivalAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeDeleteAction(mapping, form, request, response, null, null);
    }
}