package com.yuanluesoft.bidding.project.actions.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 
 * @author linchuan
 *
 */
public class SubmitAnswer extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, null, "submitted");
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.project.actions.project.admin.ProjectAction#saveProjectComponent(java.lang.String, com.yuanluesoft.bidding.project.forms.admin.Project, com.yuanluesoft.bidding.project.pojo.BiddingProject, javax.servlet.http.HttpServletRequest)
	 */
	protected void saveProjectComponent(String componentName, Project projectForm, BiddingProject project, HttpServletRequest request) throws Exception {
		if(componentName.equals("answer") && "没有".equals(projectForm.getHasAnswer())) {
			return;
		}
		super.saveProjectComponent(componentName, projectForm, project, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean)
	 */
	public Record save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean reload, String tabSelected, String actionResult) throws Exception {
		BiddingProject project = (BiddingProject)super.save(mapping, form, request, response, reload, tabSelected, actionResult);
		project.setHasAnswer(request.getParameter("hasAnswer"));
		return project;
	}
}