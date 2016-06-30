package com.yuanluesoft.job.talent.actions.schoolclassreports.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.job.talent.forms.admin.SchoolClassReports;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author chuan
 *
 */
public class SchoolClassReportsAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		SchoolClassReports reportsForm = (SchoolClassReports)form;
		if(reportsForm.getSchoolClass()==null || reportsForm.getSchoolClass().isEmpty()) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		try {
			if(!getOrgService().isOrgManager(reportsForm.getSchoolClassId(), sessionInfo)) {
				throw new PrivilegeException();
			}
		}
		catch (ServiceException e) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		SchoolClassReports reportsForm = (SchoolClassReports)form;
		if(reportsForm.getSchoolClass()!=null && !reportsForm.getSchoolClass().isEmpty()) {
			JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
			reportsForm.setReports(jobTalentService.listTalentReports(reportsForm.getSchoolClassId(), reportsForm.getSchoolClass(), reportsForm.getGraduateDate(), reportsForm.getSchoolingLength(), reportsForm.getQualification(), reportsForm.getSpecialty(), reportsForm.getTrainingMode(), reportsForm.getReportBegin(), reportsForm.getReportEnd(), reportsForm.getNoticeNumber(), reportsForm.getReportNumber()));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		SchoolClassReports reportsForm = (SchoolClassReports)form;
		if(reportsForm.getSchoolClass()!=null && !reportsForm.getSchoolClass().isEmpty()) {
			reportsForm.getFormActions().removeFormAction("下一步");
		}
		else {
			reportsForm.getFormActions().removeFormAction("保存");
			reportsForm.getFormActions().removeFormAction("打印报到证");
			reportsForm.getFormActions().removeFormAction("打印通知书");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SchoolClassReports reportsForm = (SchoolClassReports)form;
		JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
		jobTalentService.saveTalentReports(reportsForm.getSchoolClassId(), reportsForm.getSchoolClass(), request);
		return null;
	}
}