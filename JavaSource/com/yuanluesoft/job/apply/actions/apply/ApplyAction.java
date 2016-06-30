package com.yuanluesoft.job.apply.actions.apply;

import java.util.Iterator;
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
import com.yuanluesoft.job.apply.forms.Apply;
import com.yuanluesoft.job.apply.pojo.JobApply;
import com.yuanluesoft.job.company.pojo.Job;
import com.yuanluesoft.job.company.service.JobCompanyService;
import com.yuanluesoft.job.talent.pojo.JobTalent;
import com.yuanluesoft.job.talent.pojo.JobTalentSchooling;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author linchuan
 *
 */
public class ApplyAction extends FormAction {

	public ApplyAction() {
		super();
		externalAction = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(sessionInfo.getUserType()==JobTalentService.PERSON_TYPE_JOB_TALENT) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else {
			JobApply apply = (JobApply)record;
			if(sessionInfo.getUserId()==apply.getTalentId()) {
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
			else if(sessionInfo.getUnitId()==apply.getCompanyId()) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Apply applyForm = (Apply)form;
		JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
		JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
		Job job = (Job)jobCompanyService.load(Job.class, applyForm.getJobId());
		JobTalent talent = (JobTalent)jobTalentService.load(JobTalent.class, sessionInfo.getUserId());
		applyForm.setJob(job); //职位
		applyForm.setTalent(talent); //人才
		applyForm.setJobName(job.getName()); //职位名称
		applyForm.setCompany(job.getCompany().getName()); //公司名称
		applyForm.setTalentName(talent.getName()); //求职人姓名
		applyForm.setSex(talent.getSex()); //性别
		JobTalentSchooling schooling = (JobTalentSchooling)(talent.getSchoolings()==null || talent.getSchoolings().isEmpty() ? null : talent.getSchoolings().iterator().next());
		if(schooling!=null) {
			applyForm.setSchool(schooling.getSchool()); //毕业院校
			applyForm.setSpecialty(schooling.getSpecialty()); //专业
			applyForm.setQualification(schooling.getQualification()); //学历
		}
		applyForm.setWorkYear(talent.getWorkYear()); //工作年限
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		JobApply apply = (JobApply)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			form.setSubForm("submitApply");
		}
		else if(sessionInfo.getUserId()==apply.getTalentId()) {
			form.setSubForm("talentApply");
		}
		else if(sessionInfo.getUnitId()==apply.getCompanyId()) {
			form.setSubForm("companyApply");
			String formActions = null;
			if(apply.getStatus()==0) { //申请|0\0已删除|1\0拟面试|2\0已面试|3\0未录用|4\0已录用|5
				formActions = "删除,拟面试";
			}
			else if(apply.getStatus()==1) { //删除
				formActions = "拟面试";
			}
			else if(apply.getStatus()==2) { //拟面试
				formActions = "发送面试邀请函,登记面试情况,录用,不录用";
			}
			else if(apply.getStatus()==2 || apply.getStatus()==3) { //已面试
				formActions = "登记面试情况,录用,不录用";
			}
			else if(apply.getStatus()==4) { //未录用
				formActions = "录用";
			}
			else if(apply.getStatus()==5) { //已录用
				formActions = "发送录用通知书";
			}
			for(Iterator iterator = form.getFormActions().iterator(); iterator.hasNext();) {
				com.yuanluesoft.jeaf.form.model.FormAction formAction = (com.yuanluesoft.jeaf.form.model.FormAction)iterator.next();
				if(("," + formActions + ",").indexOf("," + formAction.getTitle() + ",")==-1) {
					iterator.remove();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			JobApply apply = (JobApply)record;
			JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
			JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
			Job job = (Job)jobCompanyService.load(Job.class, apply.getJobId());
			JobTalent talent = (JobTalent)jobTalentService.load(JobTalent.class, sessionInfo.getUserId());
			apply.setJobName(job.getName()); //职位名称
			apply.setCompanyId(job.getCompanyId()); //公司ID
			apply.setCompany(job.getCompany().getName()); //公司名称
			apply.setTalentId(talent.getId()); //求职人ID
			apply.setTalentName(talent.getName()); //求职人姓名
			apply.setSex(talent.getSex()); //性别
			JobTalentSchooling schooling = (JobTalentSchooling)(talent.getSchoolings()==null || talent.getSchoolings().isEmpty() ? null : talent.getSchoolings().iterator().next());
			if(schooling!=null) {
				apply.setSchool(schooling.getSchool()); //毕业院校
				apply.setSpecialty(schooling.getSpecialty()); //专业
				apply.setQualification(schooling.getQualification()); //学历
			}
			apply.setWorkYear(talent.getWorkYear()); //工作年限
			apply.setCreated(DateTimeUtils.now()); //求职时间
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}