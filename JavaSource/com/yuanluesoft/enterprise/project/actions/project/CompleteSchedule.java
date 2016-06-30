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
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class CompleteSchedule extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeRunAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.project.actions.project.ProjectAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		EnterpriseProject project = (EnterpriseProject)record;
		Project projectForm = (Project)form;
		if('0'==projectForm.getIsDesignCompleted()) {
			project.setProjectStage(projectForm.getProjectTeam().getStage());
			//创建项目组
			EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
			enterpriseProjectService.createProjectTeam(project.getId(), projectForm.getProjectTeam().getStage(), projectForm.getProjectTeam().getId(), projectForm.getProjectTeam().getWorkContent(), projectForm.getProjectTeam().getExpectingDate(), projectForm.getProjectTeam().getProjectTeamManagerIds(), projectForm.getProjectTeam().getProjectTeamManagerNames(), projectForm.getProjectTeam().getProjectTeamMemberIds(), projectForm.getProjectTeam().getProjectTeamMemberNames(), sessionInfo.getUserId(), sessionInfo.getUserName());
			//新记录,从临时附件中注销
			((TemporaryFileManageService)getService("temporaryFileManageService")).unregistTemporaryAttachment(EnterpriseProjectTeam.class.getName(), projectForm.getProjectTeam().getId());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}