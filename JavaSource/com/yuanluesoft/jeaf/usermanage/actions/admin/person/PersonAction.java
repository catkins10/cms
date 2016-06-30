/*
 * Created on 2007-4-19
 *
 */
package com.yuanluesoft.jeaf.usermanage.actions.admin.person;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
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
import com.yuanluesoft.jeaf.usermanage.forms.admin.Employee;
import com.yuanluesoft.jeaf.usermanage.forms.admin.Student;
import com.yuanluesoft.jeaf.usermanage.forms.admin.Teacher;
import com.yuanluesoft.jeaf.usermanage.pojo.Genearch;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSubjection;
import com.yuanluesoft.jeaf.usermanage.security.service.UserSecurityService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PersonAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person personForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)form;
		if(sessionInfo.getUserId()==personForm.getId()) { //不允许删除自己
			throw new PrivilegeException();
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person personForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)form;
		if(getOrgService().checkPopedom(personForm.getOrgId(), "manager", sessionInfo)) { //有完全控制权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		//根据注册用户类型,检查用户的注册权限
		String popedomName = null;
		if(personForm instanceof Student || personForm instanceof com.yuanluesoft.jeaf.usermanage.forms.admin.Genearch) {
			popedomName = "registStudent"; //注册学生权限
		}
		else if(personForm instanceof Teacher) {
			popedomName = "registTeacher"; //注册教师权限
		}
		else if(personForm instanceof Employee) {
			popedomName = "registEmployee"; //注册普通用户权限
		}
		boolean registable = (getOrgService().checkPopedom(personForm.getOrgId(), popedomName, sessionInfo));
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(registable) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		return registable && ((Person)record).getCreatorId()==sessionInfo.getUserId() ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Person person = (Person)super.loadRecord(form, formDefine, id, sessionInfo, request);
		if(person==null) {
			return null;
		}
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person personForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)form;
		//设置用户所在组织ID
		personForm.setOrgId(((PersonSubjection)person.getSubjections().iterator().next()).getOrgId());
		return person;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.jeaf.usermanage.pojo.Person person = (com.yuanluesoft.jeaf.usermanage.pojo.Person)record;
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person personForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			person.setCreated(DateTimeUtils.now()); //注册时间
			person.setCreator(sessionInfo.getUserName()); //注册人
			person.setCreatorId(sessionInfo.getUserId()); //注册人ID
		}
		else {
			//设置是否停用
			UserSecurityService userSecurityService = (UserSecurityService)getService("userSecurityService");
			userSecurityService.setHalt(person.getId(), personForm.getHalt()=='1');
			personForm.setHalt(userSecurityService.isHalt(record.getId()) ? '1' : '0');
		}
		//更新所属机构列表
		if(!(person instanceof Genearch)) {
			person.setOrgIds(request.getParameter("orgId") + (request.getParameter("otherOrgIds")==null || request.getParameter("otherOrgIds").equals("") ? "" : "," + request.getParameter("otherOrgIds")));
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Person person = (Person)record;
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person personForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)form;
		//设置用户所在组织ID
		int index = person.getOrgIds().indexOf(',');
		long orgId;
		if(index==-1) {
			orgId = Long.parseLong(person.getOrgIds());
		}
		else {
			orgId = Long.parseLong(person.getOrgIds().substring(0, index));
			//设置其他组织机构
			personForm.setOtherOrgIds(person.getOrgIds().substring(index + 1));
			personForm.setOtherOrgNames(getOrgService().getDirectoryFullNames(personForm.getOtherOrgIds(), "/", ",", "unit,school"));
		}
		personForm.setOrgId(orgId);
		personForm.setOrgFullName(getOrgService().getDirectoryFullName(personForm.getOrgId(), "/", "unit,school"));
		if(personForm.getPassword()!=null &&
		   !"".equals(personForm.getPassword()) &&
		   (!personForm.getPassword().startsWith("{") ||
		    !personForm.getPassword().endsWith("}"))) {
			personForm.setPassword("{" + personForm.getPassword() + "}");
		}
		//设置是否停用
		UserSecurityService userSecurityService = (UserSecurityService)getService("userSecurityService");
		personForm.setHalt(userSecurityService.isHalt(record.getId()) ? '1' : '0');
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person personForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)form;
		if(personForm.getSex()==0) {
			personForm.setSex('M'); //男
		}
		personForm.setOrgFullName(getOrgService().getDirectoryFullName(personForm.getOrgId(), "/", "unit,school"));
		personForm.setCreated(DateTimeUtils.now()); //注册时间
		personForm.setCreator(sessionInfo.getUserName()); //注册人
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		super.inheritProperties(newForm, currentForm);
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person newPersonForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)newForm;
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person currentPersonForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)currentForm;
		newPersonForm.setOrgId(currentPersonForm.getOrgId());
		newPersonForm.setOrgFullName(currentPersonForm.getOrgFullName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		//检查用户是否被使用
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		Person person = (Person)record;
		com.yuanluesoft.jeaf.usermanage.forms.admin.Person personForm = (com.yuanluesoft.jeaf.usermanage.forms.admin.Person)form;
		if(person.getLoginName()!=null && memberServiceList.isLoginNameInUse(person.getLoginName(), person.getId())) {
			personForm.setError("登录用户名已经被使用");
			throw new ValidateException();
		}
	}
}