package com.yuanluesoft.enterprise.project.actions.project.projectcontract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.ProjectContract;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends ProjectContractAction {
	
	 public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		 ProjectContract projectContractForm = (ProjectContract)form;
		 return executeDeleteComponentAction(mapping, form, "contract", projectContractForm.getOpenerTabPage(), "refreshProject", request, response);
	 }
}