package com.yuanluesoft.dpc.keyproject.actions.project;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.dpc.keyproject.forms.Project;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProject;
import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectDeclare;
import com.yuanluesoft.dpc.keyproject.service.KeyProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Declare extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, "申报完成", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkflowRun(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeWorkflowRun(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, Record record, SessionInfo sessionInfo) throws Exception {
		super.beforeWorkflowRun(workflowForm, request, response, record, sessionInfo);
		KeyProject project = (KeyProject)record;
		project.setDeclareOrDebrief("申报");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		Project projectForm = (Project)formToValidate;
		int year = DateTimeUtils.getYear(DateTimeUtils.date());
		if(projectForm.getDeclareYear()<year || projectForm.getDeclareYear()>year+1) {
			projectForm.setError("申报年度不正确");
			throw new ValidateException();
		}
		if(projectForm.getDeclares()!=null) {
			for(Iterator iterator = projectForm.getDeclares().iterator(); iterator.hasNext();) {
				KeyProjectDeclare declare = (KeyProjectDeclare)iterator.next();
				if(projectForm.getDeclareYear()<declare.getDeclareYear() || (projectForm.getDeclareYear()==declare.getDeclareYear() && declare.getIsKeyProject()=='1')) {
					projectForm.setError("申报年度不正确");
					throw new ValidateException();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, boolean, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		Project projectForm = (Project)workflowForm;
		KeyProject project = (KeyProject)record;
		KeyProjectService keyProjectService = (KeyProjectService)getService("keyProjectService");
		keyProjectService.declareProject(project, projectForm.getDeclareYear(), sessionInfo);
	}
}