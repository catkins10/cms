package com.yuanluesoft.job.company.actions.refreshalljobs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.dialog.actions.DialogFormAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.job.company.forms.RefreshAllJobs;
import com.yuanluesoft.job.company.service.JobCompanyService;

/**
 * 
 * @author linchuan
 *
 */
public class RefreshAllJobsAction extends DialogFormAction {

	public RefreshAllJobsAction() {
		super();
		externalAction = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkLoadPrivilege(ActionForm form, HttpServletRequest request, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(sessionInfo.getUserType()!=JobCompanyService.PERSON_TYPE_JOB_COMPANY) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initForm(form, acl, sessionInfo, request);
		RefreshAllJobs refreshAllJobsForm = (RefreshAllJobs)form;
		JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
		refreshAllJobsForm.setLastRefreshTime(jobCompanyService.getLastRefreshTime(sessionInfo.getUnitId()));
		refreshAllJobsForm.setRefreshTimesLeft(jobCompanyService.getRefreshTimesLeft(sessionInfo.getUnitId()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.dialog.actions.DialogFormAction#submitForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void submitForm(ActionForm form, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.submitForm(form, sessionInfo, request, response);
		JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
		jobCompanyService.refreshAllJobs(sessionInfo.getUnitId());
	}
}