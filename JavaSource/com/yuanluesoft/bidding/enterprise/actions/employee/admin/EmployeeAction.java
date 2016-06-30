package com.yuanluesoft.bidding.enterprise.actions.employee.admin;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.enterprise.actions.enterprise.admin.EnterpriseAction;
import com.yuanluesoft.bidding.enterprise.forms.admin.Employee;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEmployee;
import com.yuanluesoft.bidding.enterprise.pojo.BiddingEnterprise;
import com.yuanluesoft.bidding.enterprise.services.EmployeeService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class EmployeeAction extends EnterpriseAction  {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		BiddingEnterprise enterprise = (BiddingEnterprise)record;
		if(enterprise!=null && enterprise.getIsValid()=='1' && acl.contains("manageUnit_registEmployee")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#isLockByPerson(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isLockByMe(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		Employee employeeForm = (Employee)form;
		EmployeeService employeeService = (EmployeeService)getService("employeeService");
		employeeForm.getEmployee().setIsPermanent('0');
		employeeForm.getEmployee().setTryEndDate(DateTimeUtils.add(DateTimeUtils.date(), Calendar.DAY_OF_MONTH, employeeService.getTryDays())); //试用截止时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentResource(com.yuanluesoft.jeaf.form.ActionForm, org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.util.List, char, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		Employee employeeForm = (Employee)form;
		BiddingEmployee employee = (BiddingEmployee)component;
		if(OPEN_MODE_CREATE_COMPONENT.equals(employeeForm.getAct())) { //新建
			employeeForm.getFormActions().removeFormAction("写KEY");
		}
		if(employee==null || employee.getEkeyId()==null || employee.getEkeyId().isEmpty()) {
			employeeForm.getFormActions().removeFormAction("回收KEY");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Employee employeeForm = (Employee)form;
		if(employeeForm.getEmployee().getId()==0) {
			return null;
		}
		EmployeeService employeeService = (EmployeeService)getService("employeeService");
		return employeeService.load(BiddingEmployee.class, employeeForm.getEmployee().getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionMapping)
	 */
	public void validateComponent(ActionForm form, String openMode, Record mainRecord, Record component, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
		super.validateComponent(form, openMode, mainRecord, component, componentName, sessionInfo, request);
		//检查用户是否被使用
		MemberServiceList memberServiceList = (MemberServiceList)getService("memberServiceList");
		Employee employeeForm = (Employee)form;
		try {
			if(memberServiceList.isLoginNameInUse(employeeForm.getEmployee().getLoginName(), employeeForm.getEmployee().getId())) {
				employeeForm.setError("登录用户名已经被使用");
				throw new ValidateException();
			}
			//检查试用数是否超过限制
			if(employeeForm.getEmployee().getIsPermanent()!='1' && employeeForm.getEmployee().getId()==0 && employeeForm.getEmployee().getIsPermanent()!='1') {
				EmployeeService employeeService = (EmployeeService)getService("employeeService");
				if(employeeService.isTryOver(form.getId())) {
					employeeForm.setError("超出试用数量限制");
					throw new ValidateException();
				}
			}
		}
		catch (ServiceException e) {
			employeeForm.setError("校验时出错");
			throw new ValidateException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Employee employeeForm = (Employee)form;
		EmployeeService employeeService = (EmployeeService)getService("employeeService");
		employeeForm.getEmployee().setLastTransactor(sessionInfo.getUserName());
		employeeForm.getEmployee().setLastTransactorId(sessionInfo.getUserId());
		employeeForm.getEmployee().setLastTransactTime(DateTimeUtils.now());
		//设置外键值
		employeeForm.getEmployee().setEnterpriseId(employeeForm.getId());
		if("createComponent".equals(form.getAct())) { //新纪录
			employeeForm.getEmployee().setId(UUIDLongGenerator.generateId());
			employeeService.save(employeeForm.getEmployee());
		}
		else {
			employeeService.update(employeeForm.getEmployee());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void deleteComponentRecord(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		EmployeeService employeeService = (EmployeeService)getService("employeeService");
		Employee employeeForm = (Employee)form;
		employeeService.delete(employeeForm.getEmployee());
	}
}