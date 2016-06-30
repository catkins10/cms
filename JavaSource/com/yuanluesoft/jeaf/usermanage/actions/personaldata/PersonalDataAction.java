package com.yuanluesoft.jeaf.usermanage.actions.personaldata;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.forms.PersonalData;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.pojo.PersonSubjection;
import com.yuanluesoft.jeaf.usermanage.pojo.SchoolClass;
import com.yuanluesoft.jeaf.usermanage.pojo.Student;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class PersonalDataAction extends FormAction {

	public PersonalDataAction() {
		super();
		isSecureAction = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//设置用户ID
		SessionInfo sessionInfo = getSessionInfo(request, response);
		PersonalData personalSettingForm = (PersonalData)form;
		personalSettingForm.setAct("edit");
		personalSettingForm.setId(sessionInfo.getUserId());
		return super.load(form, request, response);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Person person = (Person)record;
		PersonalData personalSettingForm = (PersonalData)form;
		//用户名
		personalSettingForm.setUserName(sessionInfo.getUserName());
		//获取用户所在机构ID和全称
		if(person.getSubjections()!=null && !person.getSubjections().isEmpty()) {
			OrgService orgService = getOrgService();
			PersonSubjection subjection = (PersonSubjection)person.getSubjections().iterator().next();
			personalSettingForm.setOrgId(subjection.getOrgId());
			personalSettingForm.setOrgFullName(orgService.getDirectoryFullName(subjection.getOrgId(), "/", "unit,school"));
			//判断学生是否毕业
			if(person instanceof Student) {
				SchoolClass myClass = (SchoolClass)orgService.getOrg(personalSettingForm.getOrgId());
				if(myClass.getIsGraduated()=='1') { //属于毕业班
					personalSettingForm.setFinishSchool(true);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		//生成所属机构列表
		Person person = (Person)record;
		PersonalData personalDataForm = (PersonalData)form;
		if(personalDataForm.getOrgId()>0) {
			Set subjections = new HashSet();
			PersonSubjection subjection = new PersonSubjection();
			subjection.setId(UUIDLongGenerator.generateId());
			subjection.setPersonId(person.getId());
			subjection.setOrgId(personalDataForm.getOrgId());
			subjections.add(subjection);
			person.setSubjections(subjections);
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, org.apache.struts.action.ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		PersonalData personalSettingForm = (PersonalData)form;
		if(personalSettingForm.getOrgId()==0) {
			return;
		}
		//检查用户选择的机构是否和原来的在通一个单位或学校
		OrgService orgService = getOrgService();
		if(sessionInfo.getUnitId()==personalSettingForm.getOrgId()) { //用户选中的新的组织机构就是原来的单位
			return;
		}
		Org newUnit = orgService.getParentUnitOrSchool(personalSettingForm.getOrgId());
		if(newUnit.getId()!=sessionInfo.getUnitId()) { //用户选择了其他的单位或学校
			personalSettingForm.setError("只能选择原来所在的单位或学校");
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		throw new PrivilegeException(); //禁止删除
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkSavePrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		Person person = (Person)record;
		if(sessionInfo.getUserId()!=person.getId()) { //仅允许本人访问
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateRefeshOpenerScript(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateRefreshOpenerScript(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		return null;
	}
}