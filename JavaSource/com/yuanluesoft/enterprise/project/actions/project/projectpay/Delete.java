package com.yuanluesoft.enterprise.project.actions.project.projectpay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.ProjectPay;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends ProjectPayAction {
	
	 public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		 ProjectPay projectPay = (ProjectPay)form;
		 return executeDeleteComponentAction(mapping, form, "pay", projectPay.getOpenerTabPage(), "refreshProject", request, response);
	 }
}