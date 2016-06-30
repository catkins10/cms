package com.yuanluesoft.fet.tradestat.actions.company;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.fet.tradestat.forms.Company;
import com.yuanluesoft.fet.tradestat.model.FetSessionInfo;
import com.yuanluesoft.fet.tradestat.pojo.FetCompany;
import com.yuanluesoft.fet.tradestat.service.FetCompanyService;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class CompanyAction extends com.yuanluesoft.fet.tradestat.actions.admin.company.CompanyAction {

	public CompanyAction() {
		super();
		sessionInfoClass = FetSessionInfo.class;
		anonymousEnable = true;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getSessionInfo(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request, HttpServletResponse response) throws SessionException, SystemUnregistException {
		SessionInfo sessionInfo = super.getSessionInfo(request, response);
		if(!(sessionInfo instanceof FetSessionInfo)) {
			//不是FET会话
			throw new SessionException(SessionException.SESSION_NONE);
		}
		return sessionInfo;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL()
	 */
	protected Link getLoginPageLink(org.apache.struts.action.ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		return new Link(Environment.getWebApplicationSafeUrl() + "/fet/tradestat/statLogin.shtml", "utf-8"); 
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo = getSessionInfo(request, response);
		if(sessionInfo.getUserType()==FetCompanyService.USER_TYPE_COMPANY) { //当前登录的是企业用户
			Company companyForm = (Company)form;
			companyForm.setId(sessionInfo.getUserId());
			companyForm.setAct(OPEN_MODE_EDIT);
		}
		return super.load(form, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		throw new PrivilegeException(); //没有删除权限
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新建企业用户,允许所有人
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		FetCompany fetCompany = (FetCompany)record;
		switch(sessionInfo.getUserType()) {
		case FetCompanyService.USER_TYPE_COMPANY: //企业用户
			if(sessionInfo.getUserId()==fetCompany.getId()) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			break;
			
		case FetCompanyService.USER_TYPE_COUNTY: //区县用户
			if(sessionInfo.getUserName().equals(fetCompany.getCounty())) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			break;
			
		case FetCompanyService.USER_TYPE_DEVELOPMENT_AREA: //开发区用户
			if(sessionInfo.getUserName().equals(fetCompany.getDevelopmentArea())) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			break;
		
		default:
			if(acl.contains("application_manager")) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			else if(acl.contains("application_visitor")) {
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkSavePrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo)<RecordControlService.ACCESS_LEVEL_EDITABLE) {
			throw new PrivilegeException();
		}
	}
}