package com.yuanluesoft.job.talent.actions.talent;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.job.company.service.JobCompanyService;
import com.yuanluesoft.job.talent.forms.Talent;
import com.yuanluesoft.job.talent.pojo.JobTalent;
import com.yuanluesoft.job.talent.pojo.JobTalentCareer;
import com.yuanluesoft.job.talent.pojo.JobTalentIntention;
import com.yuanluesoft.job.talent.pojo.JobTalentSchooling;
import com.yuanluesoft.job.talent.service.JobTalentService;

/**
 * 
 * @author linchuan
 *
 */
public class TalentAction extends FormAction {

	public TalentAction() {
		super();
		externalAction = true; //对外的操作
		anonymousEnable = true; //允许匿名
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		JobTalent talent = (JobTalent)record;
		if(OPEN_MODE_CREATE.equals(openMode) || talent.getStatus()==0) { //新建,或者未审核通过
			return RecordControlService.ACCESS_LEVEL_EDITABLE; //允许所有人注册或修改
		}
		if(talent.getId()==sessionInfo.getUserId()) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE; //允许本人修改
		}
		if(sessionInfo.getUserType()==JobCompanyService.PERSON_TYPE_JOB_COMPANY || //允许企业查询
		   acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || //允许管理员查询
		   (sessionInfo.getUserType()==PersonService.PERSON_TYPE_EMPLOYEE && acl.contains(AccessControlService.ACL_APPLICATION_VISITOR))) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		JobTalent talent = (JobTalent)record;
		if(accessLevel < RecordControlService.ACCESS_LEVEL_EDITABLE) {
			form.setSubForm("talent");
		}
		else if(OPEN_MODE_CREATE.equals(openMode)) {
			form.setSubForm("regist");
		}
		else if(talent.getStatus()==2 || talent.getIntentions()==null || talent.getIntentions().isEmpty()) { //审核不通过，或者没有求职意向
			form.setSubForm("registProfileAndIntention"); //打开注册(填写个人信息和求职意向)页面
		}
		else if(talent.getStatus()==4 || talent.getSchoolings()==null || talent.getSchoolings().isEmpty()) { //审核不通过且已经重填求职意向，或者没有教育经历  talent.getCareers()==null || talent.getCareers().isEmpty()) //或者没有工作经历
			form.setSubForm("registSchoolingAndCareer"); //打开注册(填写教育和工作经历)页面
		}
		else if(talent.getStatus()==1) { //已经通过审核
			form.setSubForm("editTalent");
		}
		else { //未通过审核
			form.setSubForm("talentRegistted"); //注册完成提示
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Talent talentForm = (Talent)form;
		if(talentForm.getStatus()==2 && talentForm.getIntentions()!=null && !talentForm.getIntentions().isEmpty()) { //审核不通过
			talentForm.setIntention((JobTalentIntention)talentForm.getIntentions().iterator().next());
		}
		else if(talentForm.getStatus()==4 && talentForm.getSchoolings()!=null && !talentForm.getSchoolings().isEmpty()) { //审核不通过,且已经填写求职意向
			talentForm.setSchooling((JobTalentSchooling)talentForm.getSchoolings().iterator().next());
			if(talentForm.getCareers()!=null && !talentForm.getCareers().isEmpty()) {
				talentForm.setCareer((JobTalentCareer)talentForm.getCareers().iterator().next());
			}
		}
		else if(talentForm.getSchooling().getSchool()==null || talentForm.getSchooling().getSchool().isEmpty()) {
			SiteService siteService = (SiteService)getService("siteService");
			talentForm.getSchooling().setSchool(siteService.getParentSite(RequestUtils.getParameterLongValue(request, "siteId")).getOwnerUnitName());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		Talent talentForm = (Talent)formToValidate;
		String[] passwords = request.getParameterValues("password");
		for(int i=1; i<(passwords==null ? 0 : passwords.length); i++) {
			if(!passwords[i].equals(passwords[0])) {
				talentForm.setError("密码不一致");
				throw new ValidateException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		//检查用户是否被使用
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		JobTalent talent = (JobTalent)record;
		Talent talentForm = (Talent)form;
		if(talent.getEmail()!=null && memberServiceList.isLoginNameInUse(talent.getEmail(), talent.getId())) {
			talentForm.setError("邮箱已经被注册");
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		JobTalent talent = (JobTalent)record;
		talent.setLastModified(DateTimeUtils.now());
		talent = (JobTalent)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(OPEN_MODE_CREATE.equals(openMode) && talent.getStatus()==1) { //注册、且已经通过审核,创建新的会话
			SessionService sessionService = (SessionService)getService("sessionService");
			sessionInfo = sessionService.setSessionInfo(talent.getEmail(), SessionInfo.class, request);
		}
		boolean intentionCreated = talent.getStatus()!=2 && talent.getIntentions()!=null && !talent.getIntentions().isEmpty(); //有无求职意向
		boolean schoolingCreated = talent.getStatus()!=2 && talent.getStatus()!=4 && talent.getSchoolings()!=null && !talent.getSchoolings().isEmpty(); //有无教育经历
		boolean careerCreated = talent.getStatus()!=2 && talent.getStatus()!=4 && talent.getCareers()!=null && !talent.getCareers().isEmpty(); //有无工作经历
		Talent talentForm = (Talent)form;
		if(intentionCreated && schoolingCreated) { // && careerCreated) { //已经完善资料
			form.setSubForm("talentSubmitted"); //提交成功
			return record;
		}
		JobTalentService jobTalentService = (JobTalentService)getService("jobTalentService");
		if(!intentionCreated && talentForm.getIntention().getIndustryNames()!=null) { //求职意向行业不为空,说明有配置求职意向
			//保存求职意向
			if(talentForm.getIntention().getId()!=0) {
				jobTalentService.update(talentForm.getIntention());
			}
			else {
				talentForm.getIntention().setId(UUIDLongGenerator.generateId());
				talentForm.getIntention().setTalentId(talent.getId());
				jobTalentService.save(talentForm.getIntention());
			}
			intentionCreated = true;
			if(talent.getStatus()==2) {
				talent.setStatus(4);
				jobTalentService.update(talent);
			}
		}
		if(!schoolingCreated && talentForm.getSchooling().getSchool()!=null) { //学校不为空,说明有配置教育经历
			//保存教育经历
			if(talentForm.getSchooling().getId()!=0) {
				jobTalentService.update(talentForm.getSchooling());
			}
			else {
				talentForm.getSchooling().setId(UUIDLongGenerator.generateId());
				talentForm.getSchooling().setTalentId(talent.getId());
				jobTalentService.save(talentForm.getSchooling());
			}
			schoolingCreated = true;
			if(talent.getStatus()==4) {
				talent.setStatus(0); //恢复为注册状态
				jobTalentService.update(talent);
			}
		}
		if(!careerCreated && talentForm.getCareer().getCompany()!=null) { //公司不为空,说明有配置工作经历
			//保存工作经历
			if(talentForm.getCareer().getId()!=0) {
				jobTalentService.update(talentForm.getCareer());
			}
			else {
				talentForm.getCareer().setId(UUIDLongGenerator.generateId());
				talentForm.getCareer().setTalentId(talent.getId());
				jobTalentService.save(talentForm.getCareer());
			}
			careerCreated = true;
		}
		if(intentionCreated && schoolingCreated) { // && careerCreated) {
			talentForm.setStatus(talent.getStatus());
			form.setSubForm("talentRegistted"); //完成注册
		}
		return record;
	}
}