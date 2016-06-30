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
public class Save extends ProjectPayAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProjectPay projectPay = (ProjectPay)form;
		return executeSaveComponentAction(mapping, form, "pay", projectPay.getOpenerTabPage(), "projectId", "refreshProject", false, request, response);
	}
}