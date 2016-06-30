package com.yuanluesoft.job.company.actions.company;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.job.company.forms.Company;
import com.yuanluesoft.job.company.pojo.JobCompany;
import com.yuanluesoft.job.company.service.JobCompanyService;

/**
 * 
 * @author linchuan
 *
 */
public class CompanyAction extends com.yuanluesoft.job.company.actions.company.admin.CompanyAction {

	public CompanyAction() {
		super();
		externalAction = true; //对外的操作
		anonymousEnable = true; //允许匿名
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			return RecordControlService.ACCESS_LEVEL_EDITABLE; //允许所有人注册
		}
		if(record.getId()==sessionInfo.getUnitId()) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE; //允许本企业的用户修改
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(accessLevel<RecordControlService.ACCESS_LEVEL_EDITABLE) {
			form.setSubForm("company");
		}
		else if(OPEN_MODE_CREATE.equals(openMode)) {
			form.setSubForm("registCompany");
		}
		else if(OPEN_MODE_EDIT.equals(openMode)) {
			form.setSubForm("editCompany");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		Company companyForm = (Company)formToValidate;
		String[] passwords = request.getParameterValues("password");
		for(int i=1; i<(passwords==null ? 0 : passwords.length); i++) {
			if(!passwords[i].equals(passwords[0])) {
				companyForm.setError("密码不一致");
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
		JobCompany company = (JobCompany)record;
		Company companyForm = (Company)form;
		if(company.getLoginName()!=null && memberServiceList.isLoginNameInUse(company.getLoginName(), company.getId())) {
			companyForm.setError("登录用户名已经被使用");
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Company companyForm = (Company)form;
		JobCompany company = (JobCompany)record;
		JobCompanyService jobCompanyService = (JobCompanyService)getService("jobCompanyService");
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建
			company.setIp(RequestUtils.getRemoteAddress(request));
		}
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(OPEN_MODE_CREATE.equals(openMode)) {
			form.setSubForm("companyRegistted"); //完成注册
		}
		else if(OPEN_MODE_EDIT.equals(openMode)) {
			form.setSubForm("companySubmitted"); //提交成功
		}
		jobCompanyService.updateCompanyIndustries(company, companyForm.getIndustryIds(), companyForm.getIndustryNames());
		return record;
	}
}