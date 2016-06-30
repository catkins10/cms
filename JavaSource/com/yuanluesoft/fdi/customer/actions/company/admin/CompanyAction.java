package com.yuanluesoft.fdi.customer.actions.company.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.fdi.customer.forms.admin.Company;
import com.yuanluesoft.fdi.customer.pojo.FdiCustomerCompany;
import com.yuanluesoft.fdi.customer.service.FdiCustomerService;
import com.yuanluesoft.fdi.industry.service.FdiIndustryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class CompanyAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Company companyForm = (Company)form;
		FdiCustomerCompany company = (FdiCustomerCompany)record;
		String industryIds = companyForm.getIndustryIds();
		if((industryIds==null || industryIds.isEmpty()) && company!=null) {
			industryIds = ListUtils.join(company.getIndustries(), "industryId", ",", false);
		}
		if(industryIds==null || industryIds.isEmpty()) { //新记录,不做权限控制
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		//检查用户对行业的权限
		FdiIndustryService fdiIndustryService = (FdiIndustryService)getService("fdiIndustryService");
		char level;
		try {
			level = fdiIndustryService.getIndustryAccessLevel(industryIds, sessionInfo);
		}
		catch (ServiceException e) {
			throw new PrivilegeException(e);
		}
		if(level>RecordControlService.ACCESS_LEVEL_READONLY) { //有编辑权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(level>=RecordControlService.ACCESS_LEVEL_READONLY || acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //有查询权限或者是管理员
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		form.getTabs().addTab(-1, "contacts", "客户联系人", "contacts.jsp", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Company companyForm = (Company)form;
		companyForm.setIndustryIds(ListUtils.join(companyForm.getIndustries(), "industryId", ",", false));
		companyForm.setIndustryNames(ListUtils.join(companyForm.getIndustries(), "industry", ",", false));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		FdiCustomerCompany company = (FdiCustomerCompany)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Company companyForm = (Company)form;
		//更新所属行业
		FdiCustomerService fdiCustomerService = (FdiCustomerService)getService("fdiCustomerService");
		fdiCustomerService.saveOrUpdateIndustries(company.getId(), companyForm.getIndustryIds(), companyForm.getIndustryNames());
		return company;
	}
}