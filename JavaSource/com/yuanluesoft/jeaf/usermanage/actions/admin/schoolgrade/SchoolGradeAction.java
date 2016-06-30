package com.yuanluesoft.jeaf.usermanage.actions.admin.schoolgrade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.forms.admin.SchoolGrade;
import com.yuanluesoft.jeaf.usermanage.pojo.School;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;

/**
 * 
 * @author linchuan
 *
 */
public class SchoolGradeAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		SchoolGrade schoolGradeForm = (SchoolGrade)form;
		OrgService orgService = (OrgService)getService("orgService");
		//注册班级
		String gradeName = schoolGradeForm.getGradeName();
		if(gradeName.startsWith("小学")) {
			gradeName = gradeName.substring(2, 4);
		}
		else if(gradeName.startsWith("初中")) {
			gradeName = gradeName.substring(2, 3);
			if("一二三".indexOf(gradeName)!=-1) {
				gradeName = "初" + gradeName; //初一、初二、初三
			}
			else {
				gradeName = gradeName + "年"; //七年、八年、九年
			}
		}
		else if(gradeName.startsWith("高中")) {
			gradeName = "高" + gradeName.substring(2, 3);
		}
		for(int i=1; i<=schoolGradeForm.getClassCount(); i++) {
			try {
				orgService.addClass(gradeName + "（" + i + "）班", i, schoolGradeForm.getEnrollTime(), schoolGradeForm.getLengthOfSchooling(), schoolGradeForm.getParentOrgId(), sessionInfo.getUserId(), sessionInfo.getUserName(), null);
			}
			catch(ServiceException e) {
				
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
	 */
	public void inheritProperties(ActionForm newForm, ActionForm currentForm) {
		super.inheritProperties(newForm, currentForm);
		//继承上级机构
		((SchoolGrade)newForm).setParentOrgId(((SchoolGrade)currentForm).getParentOrgId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(com.yuanluesoft.jeaf.form.ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		OrgService orgService = (OrgService)getService("orgService");
		SchoolGrade schoolGradeForm = (SchoolGrade)form;
		//设置年级列表
		School school = (School)getOrgService().getOrg(schoolGradeForm.getParentOrgId());
		List grades;
		if("完全中学".equals(school.getCategory())) {
			grades = orgService.listGradesByStage("初中");
			grades.add("初中七年级");
			grades.add("初中八年级");
			grades.add("初中九年级");
			grades.addAll(orgService.listGradesByStage("高中"));
		}
		else {
			grades = orgService.listGradesByStage(school.getCategory());
			if("初中".equals(school.getCategory())) {
				grades.add("初中七年级");
				grades.add("初中八年级");
				grades.add("初中九年级");
			}
		}
		schoolGradeForm.setGradeNames(grades);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		OrgService orgService = (OrgService)getService("orgService");
		if(orgService.checkPopedom(((SchoolGrade)form).getParentOrgId(), "manager", sessionInfo)) {
			throw new PrivilegeException();
		}
	}
}
