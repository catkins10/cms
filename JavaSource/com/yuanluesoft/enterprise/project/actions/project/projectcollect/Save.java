package com.yuanluesoft.enterprise.project.actions.project.projectcollect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.ProjectCollect;

/**
 * 
 * @author linchuan
 *
 */
public class Save extends ProjectCollectAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProjectCollect projectCollect = (ProjectCollect)form;
		return executeSaveComponentAction(mapping, form, "collect", projectCollect.getOpenerTabPage(), "projectId", "refreshProject", false, request, response);
	}
}