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
public class Save extends ProjectContractAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProjectContract projectContractForm = (ProjectContract)form;
		return executeSaveComponentAction(mapping, form, "contract", projectContractForm.getOpenerTabPage(), "projectId", "refreshProject", false, request, response);
	}
}