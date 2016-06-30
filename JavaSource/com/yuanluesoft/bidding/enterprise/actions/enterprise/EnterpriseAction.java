package com.yuanluesoft.bidding.enterprise.actions.enterprise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.bidding.enterprise.forms.Enterprise;
import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.bidding.enterprise.services.EmployeeService;
import com.yuanluesoft.bidding.enterprise.services.EnterpriseService;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author chuan
 *
 */
public class EnterpriseAction extends com.yuanluesoft.bidding.enterprise.actions.enterprise.admin.EnterpriseAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL()
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		return new Link(Environment.getWebApplicationSafeUrl() + "/bidding/enterprise/login.shtml" + (siteId>0 ? "?siteId=" + siteId : ""), "utf-8"); 
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.actions.enterprise.admin.EnterpriseAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		char accessLevel = RecordControlService.ACCESS_LEVEL_NONE;
		try {
			accessLevel = super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
		}
		catch(PrivilegeException pe) {
		
		}
		if(accessLevel>=RecordControlService.ACCESS_LEVEL_READONLY) {
			return accessLevel;
		}
		if(record.getId()==sessionInfo.getDepartmentId()) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		return accessLevel;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		super.checkSavePrivilege(form, request, record, openMode, acl, sessionInfo);
		BiddingEnterprise enterprise = (BiddingEnterprise)record;
		if(enterprise.getIsAlter()=='1' && enterprise.getAlterEnterpriseId()!=sessionInfo.getDepartmentId()) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(com.yuanluesoft.jeaf.form.ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Record record = super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(record==null && (sessionInfo instanceof BiddingSessionInfo)) {
			EnterpriseService enterpriseService = (EnterpriseService)getService("enterpriseService");
			record = enterpriseService.load(BiddingEnterprise.class, sessionInfo.getDepartmentId());
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.actions.enterprise.admin.EnterpriseAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Enterprise enterpriseForm = (Enterprise)form;
		enterpriseForm.getTabs().removeTab("workflowLog"); //不显示流程页签
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.String, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		if(OPEN_MODE_CREATE.equals(openMode)) {
			Enterprise enterpriseForm = (Enterprise)form;
			//检查用户名是否被占用
			EmployeeService employeeService = (EmployeeService)getService("employeeService");
			if(employeeService.isLoginNameInUse(enterpriseForm.getLoginName(), 0)) {
				throw new ValidateException("用户名“" + enterpriseForm.getLoginName() + "”已被占用");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.bidding.enterprise.actions.enterprise.admin.EnterpriseAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(request.getAttribute("EmployeeAdded")==null && OPEN_MODE_CREATE.equals(openMode)) {
			Enterprise enterpriseForm = (Enterprise)form;
			//注册用户
			EmployeeService employeeService = (EmployeeService)getService("employeeService");
			employeeService.addEmplyee((BiddingEnterprise)record, enterpriseForm.getLoginName(), enterpriseForm.getPassword());
			request.setAttribute("EmployeeAdded", Boolean.TRUE);
			SessionService sessionService = (SessionService)getService("sessionService");
			sessionInfo = sessionService.setSessionInfo(enterpriseForm.getLoginName(), sessionInfoClass, request);
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}