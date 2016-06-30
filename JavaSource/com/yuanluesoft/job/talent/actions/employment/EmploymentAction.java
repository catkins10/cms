package com.yuanluesoft.job.talent.actions.employment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.job.talent.actions.component.TalentComponentAction;
import com.yuanluesoft.job.talent.forms.Employment;
import com.yuanluesoft.job.talent.pojo.JobTalent;
import com.yuanluesoft.job.talent.pojo.JobTalentSchooling;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author linchuan
 *
 */
public class EmploymentAction extends TalentComponentAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Employment employment = (Employment)form;
		JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
		SiteService siteService = (SiteService)getService("siteService");
		JobTalent jobTalent = (JobTalent)jobTalentService.load(JobTalent.class, sessionInfo.getUserId());
		employment.setName(jobTalent.getName()); //姓名
		employment.setStudentSource(jobTalent.getStudentSource()); //生源所在地
		employment.setTel(jobTalent.getCell()); //联系方式
		employment.setEmail(jobTalent.getEmail()); //电子邮箱
		//获取学校
		JobTalentSchooling schooling = (JobTalentSchooling)ListUtils.findObjectByProperty(jobTalent.getSchoolings(), "school", siteService.getParentSite(RequestUtils.getParameterLongValue(request, "siteId")).getOwnerUnitName());
		employment.setGraduationYear(schooling!=null && schooling.getEndDate()!=null ? DateTimeUtils.getYear(schooling.getEndDate()) : DateTimeUtils.getYear(DateTimeUtils.now()) + (DateTimeUtils.getMonth(DateTimeUtils.now())>6 ? 1 : 0)); //毕业年份
		if(schooling!=null) {
			employment.setSchoolClass(schooling.getSchoolClass()); //专业（班级）
			employment.setStudentNumber(schooling.getStudentNumber()); //学号
		}
	}
}