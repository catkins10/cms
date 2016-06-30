package com.yuanluesoft.credit.regist.actions.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.credit.regist.forms.admin.CreditUser;
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
import com.yuanluesoft.jeaf.util.DateTimeUtils;
/**
 * 
 * @author zyh
 *
 */
public class RegistAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		CreditUser userForm = (CreditUser)form;
		if(acl.contains("application_manager")||userForm.getId()==sessionInfo.getUserId()) { //管理员
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		CreditUser userForm = (CreditUser)form;
		if(userForm.getPassword()!=null &&
				   !"".equals(userForm.getPassword()) &&
				   (!userForm.getPassword().startsWith("{") ||
				    !userForm.getPassword().endsWith("}"))) {
			userForm.setPassword("{" + userForm.getPassword() + "}");
				}
	}
	
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		//检查用户是否被使用
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		com.yuanluesoft.credit.regist.pojo.admin.CreditUser user = (com.yuanluesoft.credit.regist.pojo.admin.CreditUser)record;
		CreditUser userForm = (CreditUser)form;
		if(user.getLoginName()!=null && memberServiceList.isLoginNameInUse(user.getLoginName(), user.getId())) {
			userForm.setError("登录用户名已经被使用");
			throw new ValidateException();
		}
		if(user.getStatus()==2 && (user.getRemark()==null || user.getRemark().equals(""))){
			userForm.setError("审核意见不能为空");
			throw new ValidateException();
		}
	}
	
	
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.credit.regist.pojo.admin.CreditUser user = (com.yuanluesoft.credit.regist.pojo.admin.CreditUser)record;
		if(user.getStatus()==1||user.getStatus()==2){
			user.setApproveDate(DateTimeUtils.now());
			user.setApprover(sessionInfo.getUserName());
			user.setApproverId(sessionInfo.getUserId());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		// TODO 自动生成方法存根
		com.yuanluesoft.credit.regist.pojo.admin.CreditUser user = (com.yuanluesoft.credit.regist.pojo.admin.CreditUser)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(user!=null){
			DatabaseService databaseService = (DatabaseService)getService("databaseService");
			Person person = (Person)databaseService.findRecordById(Person.class.getName(), user.getId());
			if(person!=null){
				user.setPassword(person.getPassword());
			}
		}
		return user;
	}

	
}