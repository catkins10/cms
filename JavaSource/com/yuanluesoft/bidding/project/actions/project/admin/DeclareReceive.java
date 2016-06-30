package com.yuanluesoft.bidding.project.actions.project.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.service.BiddingProjectParameterService;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 完成报建受理
 * @author linchuan
 *
 */
public class DeclareReceive extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, null, null);
    }
    
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, boolean, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Project projectForm = (Project)workflowForm;
		BiddingProject project = (BiddingProject)record;
		//报建受理编号
		if(projectForm.getDeclare().getReceiveNumber()==null || projectForm.getDeclare().getReceiveNumber().equals("")) {
			BiddingProjectParameterService parameterService = (BiddingProjectParameterService)getService("biddingProjectParameterService");
			projectForm.getDeclare().setReceiveNumber(parameterService.generateDeclareReceiveNumber(project, false));
		}
		projectForm.getDeclare().setDeclarePersonId(sessionInfo.getUserId());
		projectForm.getDeclare().setDeclarePerson(sessionInfo.getUserName());
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		biddingProjectService.saveProjectComponent(project.getId(), project.getProjectName(), project.getBiddingMode(), projectForm.getDeclare());
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}