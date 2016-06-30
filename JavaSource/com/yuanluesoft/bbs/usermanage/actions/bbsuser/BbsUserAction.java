package com.yuanluesoft.bbs.usermanage.actions.bbsuser;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.bbs.base.actions.BbsFormAction;
import com.yuanluesoft.bbs.usermanage.forms.BbsUser;
import com.yuanluesoft.bbs.usermanage.model.BbsSessionInfo;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.member.pojo.Member;
import com.yuanluesoft.jeaf.usermanage.member.service.MemberService;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 * 
 * @author linchuan
 *
 */
public class BbsUserAction extends BbsFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionInfo sessionInfo = getSessionInfo(request, response);
		BbsUser bbsUserForm = (BbsUser)form;
		bbsUserForm.setAct("edit");
		bbsUserForm.setId(sessionInfo.getUserId());
    	return super.load(form, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		return getBusinessService(formDefine).load(com.yuanluesoft.bbs.usermanage.pojo.BbsUser.class, sessionInfo.getUserId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateForm(formToValidate, record, openMode, sessionInfo, request);
		if(!((BbsSessionInfo)sessionInfo).isSystemUser()) { //网上注册用户
			List errors = new ArrayList();
			//真实用户名、邮箱不能为空
			BbsUser bbsUserForm = (BbsUser)formToValidate;
			if(bbsUserForm.getName()==null || bbsUserForm.getName().equals("")) {
				errors.add("真实用户名不能为空");
			}
			if(bbsUserForm.getEmail()==null || bbsUserForm.getEmail().equals("")) {
				errors.add("邮箱不能为空");
			}
			if(!errors.isEmpty()) {
				bbsUserForm.setErrors(errors);
				throw new ValidateException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response); //默认仅填充BbsUser
		BbsUser bbsUserForm = (BbsUser)form;
		//获取用户信息
		Object person;
		if(((BbsSessionInfo)sessionInfo).isSystemUser()) { //系统用户
			PersonService personService = (PersonService)getService("personService");
			person = personService.getPerson(sessionInfo.getUserId());
		}
		else { //网上注册用户
			MemberService memberService = (MemberService)getService("memberService");
			person = memberService.getMemberById(sessionInfo.getUserId());
		}
		PropertyUtils.copyProperties(bbsUserForm, person);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo); //默认仅保存BbsUser
		//保存用户信息
		if(((BbsSessionInfo)sessionInfo).isSystemUser()) { //系统用户
			PersonService personService = (PersonService)getService("personService");
			Person person = personService.getPerson(sessionInfo.getUserId());
			fillPerson(person, form, request);
			personService.update(person);
		}
		else {
			MemberService memberService = (MemberService)getService("memberService");
			Member member = memberService.getMemberById(sessionInfo.getUserId());
			fillPerson(member, form, request);
			memberService.update(member);
		}
		return record;
	}
	
	/**
	 * 填充用户POJO
	 * @param person
	 * @param form
	 * @param request
	 * @throws Exception
	 */
	private void fillPerson(Object person, ActionForm form, HttpServletRequest request) throws Exception {
		Enumeration parameterNames = request.getParameterNames() ;
		while(parameterNames.hasMoreElements()) {
			String parameterName = (String)parameterNames.nextElement();
			if(PropertyUtils.isWriteable(person, parameterName)) {
				PropertyUtils.setProperty(person, parameterName, PropertyUtils.getProperty(form, parameterName));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.setDisplayMode(null);
	}
}
