package com.yuanluesoft.enterprise.project.actions.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.project.forms.Project;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProject;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class DoCompleteDesign extends ProjectAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeRunAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.actions.project.ProjectAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Project projectForm = (Project)form;
		if(projectForm.getProjectTeam().getCompletionDescription()!=null && !projectForm.getProjectTeam().getCompletionDescription().equals("")) {
			EnterpriseProject project = (EnterpriseProject)record;
			EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
			EnterpriseProjectTeam currentTeam = getCurrentTeam(project);
			currentTeam.setCompletionDate(projectForm.getProjectTeam().getCompletionDate()==null ? DateTimeUtils.date() : projectForm.getProjectTeam().getCompletionDate());
			currentTeam.setCompletionDescription(projectForm.getProjectTeam().getCompletionDescription());
			enterpriseProjectService.update(currentTeam);
			projectForm.getProjectTeam().setCompletionDescription(null);
		}
		return record;
	}
}