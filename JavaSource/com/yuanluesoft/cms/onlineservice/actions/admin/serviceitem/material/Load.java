package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem.material;

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
public class Load extends ServiceItemMaterialAction {
	
	 public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		 return executeLoadComponentAction(mapping, form, "material", request, response);
	 }
}