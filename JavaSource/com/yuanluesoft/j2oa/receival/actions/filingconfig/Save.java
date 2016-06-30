package com.yuanluesoft.j2oa.receival.actions.filingconfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author linchuan
 *
 */
public class Save extends FilingConfigAction {
    
	public ActionForward execute(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSaveAction(mapping, form, request, response, false, null, null, null);
	}
}