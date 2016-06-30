package com.yuanluesoft.job.company.actions.pushjob;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.job.company.actions.job.JobAction;
import com.yuanluesoft.job.company.forms.PushJob;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.company.pojo.JobCompanyMailTemplate;
import com.yuanluesoft.job.company.service.JobCompanyService;
import com.yuanluesoft.job.company.service.JobParameterService;

/**
 * 
 * @author chuan
 *
 */
public class PushJobAction extends JobAction {

	public PushJobAction() {
		super();
		anonymousEnable = false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		char level = OPEN_MODE_CREATE_COMPONENT.equals(form.getAct()) ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
		if(acl.contains("manageUnit_approval")) { //后审核权限
			return level;
		}
		char inheritLevel = super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
		return inheritLevel < level ? inheritLevel : level;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#isLockByMe(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isLockByMe(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		PushJob pushJobForm = (PushJob)form;
		if(pushJobForm.getMailSubject()!=null && !pushJobForm.getMailSubject().isEmpty()) {
			return;
		}
		JobParameterService jobParameterService = (JobParameterService)getService("jobParameterService");
		JobCompanyMailTemplate mailTemplate = jobParameterService.getMailTemplateByCompanyId(sessionInfo.getUnitId());
		if(mailTemplate==null || mailTemplate.getPushMailTemplate()==null || mailTemplate.getPushMailTemplate().isEmpty()) {
			mailTemplate = jobParameterService.getMailTemplateByCompanyId(0);
		}
		if(mailTemplate!=null) {
			Job job = (Job)mainRecord;
			pushJobForm.setMailSubject(mailTemplate.getPushMailSubject().replaceAll("<职位>", job.getName())
					   													.replaceAll("<企业名称>", job.getCompany().getName()));
			String mailBody = mailTemplate.getPushMailTemplate();
			//字段替换:求职人姓名\0企业名称\0职位\0职位描述\0职位要求\0月薪\0学历要求\0工作年限要求\0发送时间\0职位链接\0求职链接\0取消订阅链接
			mailBody = mailBody.replaceAll("<职位>", job.getName())
							   .replaceAll("<企业名称>", job.getCompany().getName())
							   .replaceAll("<职位描述>", job.getDescription()==null ? "" : job.getDescription())
							   .replaceAll("<职位要求>", job.getRequirement()==null ? "" : job.getRequirement())
							   .replaceAll("<月薪>", job.getMonthlyPayRange())
							   .replaceAll("<学历要求>", FieldUtils.formatFieldValue(new Integer(job.getQualification()), FieldUtils.getFormField(form.getFormDefine(), "qualification", request), job, true, null, false, false, false, 0, null, null, request))
							   .replaceAll("<工作年限要求>", FieldUtils.formatFieldValue(new Integer(job.getWorkYear()), FieldUtils.getFormField(form.getFormDefine(), "workYear", request), job, true, null, false, false, false, 0, null, null, request))
							   .replaceAll("<发送时间>", DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyy-MM-dd"));
			pushJobForm.setMailContent(mailBody);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
		PushJob pushJobForm = (PushJob)form;
		jobCompanyService.pushJob((Job)mainRecord, pushJobForm.getMailSubject(), pushJobForm.getMailContent(), pushJobForm.getRange()==0 ? null : pushJobForm.getJobPush().getReceiverIds(), pushJobForm.getRange()==0 ? null : pushJobForm.getJobPush().getReceivers(), sessionInfo, request);
	}
}