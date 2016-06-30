package com.yuanluesoft.job.company.actions.job;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.job.company.forms.Job;
import com.yuanluesoft.job.company.service.JobCompanyService;

/**
 * 
 * @author linchuan
 *
 */
public class JobAction extends FormAction {

	public JobAction() {
		super();
		externalAction = true;
		anonymousEnable = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Job jobForm = (Job)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(sessionInfo.getUnitId()==jobForm.getCompanyId()) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		com.yuanluesoft.job.company.pojo.Job job = (com.yuanluesoft.job.company.pojo.Job)record;
		if(sessionInfo.getUnitId()==job.getCompanyId()) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(job.getIsPublic()==1) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.setSubForm(RecordControlService.ACCESS_LEVEL_EDITABLE==accessLevel ? "jobEdit" : "job");
		if(record!=null) {
			com.yuanluesoft.job.company.pojo.Job job = (com.yuanluesoft.job.company.pojo.Job)record;
			if(job.getIsPublic()==1 && accessLevel==RecordControlService.ACCESS_LEVEL_READONLY) {
				job.setQueryConnt(job.getQueryConnt() + 1);
				JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
				jobCompanyService.update(job);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.job.company.pojo.Job job = (com.yuanluesoft.job.company.pojo.Job)record;
		if(job.getIsPublic()==1) {
			if(job.getPublicTime()==null) {
				job.setPublicTime(DateTimeUtils.now());
			}
			if(job.getRefreshTime()==null) {
				job.setRefreshTime(DateTimeUtils.now());
			}
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}