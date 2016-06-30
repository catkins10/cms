package com.yuanluesoft.j2oa.personnel.actions.employee;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.personnel.forms.Employee;
import com.yuanluesoft.j2oa.personnel.pojo.PersonnelEmployee;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class EmployeeAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manageUnit_personnelSupervisor")) { //人事专员
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode) || !acl.contains(AccessControlService.ACL_APPLICATION_VISITOR)) {
			throw new PrivilegeException();
		}
		return RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Employee employeeForm = (Employee)form;
		employeeForm.setSex('M'); //性别
		employeeForm.setNation("汉"); //民族
		employeeForm.setMaritalStatus("已婚"); //婚姻状况,已婚、未婚
		employeeForm.setCreator(sessionInfo.getUserName()); //登记人
		employeeForm.setCreated(DateTimeUtils.now()); //登记时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		form.getTabs().addTab(-1, "dutyChanges", "岗位变动情况", "dutyChanges.jsp", false);
		form.getTabs().addTab(-1, "rewardsPunishments", "奖惩情况", "rewardsPunishments.jsp", false);
		form.getTabs().addTab(-1, "educations", "学习经历", "educations.jsp", false);
		form.getTabs().addTab(-1, "employments", "工作经历", "employments.jsp", false);
		form.getTabs().addTab(-1, "trainings", "培训经历", "trainings.jsp", false);
		form.getTabs().addTab(-1, "certificates", "持有证书和资质情况", "certificates.jsp", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			PersonnelEmployee employee = (PersonnelEmployee)record;
			employee.setCreatorId(sessionInfo.getUserId()); //登记人ID
			employee.setCreator(sessionInfo.getUserName()); //登记人
			employee.setCreated(DateTimeUtils.now()); //登记时间
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}