package com.yuanluesoft.credit.enterprise.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.credit.enterprise.forms.Enterprise;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;

/**
 * 
 * @author zyh
 *
 */
public class EnterpriseAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")||acl.contains("manageUnit_regist")) { //管理员
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(openMode.equals("edit")){
			com.yuanluesoft.credit.enterprise.pojo.Enterprise enterprise = (com.yuanluesoft.credit.enterprise.pojo.Enterprise)record;
			if(enterprise.getId()==sessionInfo.getUserId()){
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}
	
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Enterprise enterpriseForm = (Enterprise)form;
		if(enterpriseForm.getPassword()!=null &&
				   !"".equals(enterpriseForm.getPassword()) &&
				   (!enterpriseForm.getPassword().startsWith("{") ||
				    !enterpriseForm.getPassword().endsWith("}"))) {
			enterpriseForm.setPassword("{" + enterpriseForm.getPassword() + "}");
				}
	}
	
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		//检查用户是否被使用
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		com.yuanluesoft.credit.enterprise.pojo.Enterprise enterprise = (com.yuanluesoft.credit.enterprise.pojo.Enterprise)record;
		Enterprise enterpriseForm = (Enterprise)form;
		if(enterprise.getLoginName()!=null && memberServiceList.isLoginNameInUse(enterprise.getLoginName(), enterprise.getId())) {
			enterpriseForm.setError("登录用户名已经被使用");
			throw new ValidateException();
		}
	}
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		// TODO 自动生成方法存根
		com.yuanluesoft.credit.enterprise.pojo.Enterprise enterprise = (com.yuanluesoft.credit.enterprise.pojo.Enterprise)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(enterprise!=null){
			DatabaseService databaseService = (DatabaseService)getService("databaseService");
			Person person = (Person)databaseService.findRecordById(Person.class.getName(), enterprise.getId());
			enterprise.setPassword(person.getPassword());
		}
		return enterprise;
	}
	
	
	
}