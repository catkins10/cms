package com.yuanluesoft.enterprise.project.actions.project.projectteamplan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.ProjectTeamPlan;

/**
 * 
 * @author linchuan
 *
 */
public class Delete extends ProjectTeamPlanAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ProjectTeamPlan planForm = (ProjectTeamPlan)form;
        return executeDeleteComponentAction(mapping, form, "plan", "teamPlan" + planForm.getPlan().getTeamId(), "refreshProject", request, response);
    }
}