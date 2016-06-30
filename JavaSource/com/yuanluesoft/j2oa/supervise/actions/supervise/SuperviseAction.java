package com.yuanluesoft.j2oa.supervise.actions.supervise;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.supervise.forms.Supervise;
import com.yuanluesoft.j2oa.supervise.service.SuperviseService;
import com.yuanluesoft.jeaf.base.model.user.Person;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.user.ParticipantDepartment;

/**
 * 
 * @author linchuan
 *
 */
public class SuperviseAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runSupervise";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		com.yuanluesoft.j2oa.supervise.pojo.Supervise supervise = (com.yuanluesoft.j2oa.supervise.pojo.Supervise)record;
		if(supervise!=null && supervise.getStatus()==SuperviseService.SUPERVISE_STATUS_PROCESSING && supervise.getCreatorId()==sessionInfo.getUserId()) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //允许管理员删除
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.j2oa.supervise.pojo.Supervise supervise = (com.yuanluesoft.j2oa.supervise.pojo.Supervise)record;
		if(supervise==null || supervise.getStatus()!=SuperviseService.SUPERVISE_STATUS_PROCESSING || supervise.getCreatorId()!=sessionInfo.getUserId()) {
			form.getFormActions().removeFormAction("调整时限");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Supervise superviseForm = (Supervise)form;
		//生成督办号
		NumerationService numerationService = (NumerationService)getService("numerationService");
		superviseForm.setSn(numerationService.generateNumeration("督办", "督办号", "<序号>", true, null));
		superviseForm.setCreator(sessionInfo.getUserName());
		superviseForm.setCreated(DateTimeUtils.now());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.j2oa.supervise.pojo.Supervise supervise = (com.yuanluesoft.j2oa.supervise.pojo.Supervise)record;
		Supervise superviseForm = (Supervise)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			//生成督办号
			NumerationService numerationService = (NumerationService)getService("numerationService");
			supervise.setSn(numerationService.generateNumeration("督办", "督办号", "<序号>", false, null));
			supervise.setCreator(sessionInfo.getUserName());
			supervise.setCreatorId(sessionInfo.getUserId());
			supervise.setCreated(DateTimeUtils.now());
		}
		//设置主管领导姓名
		if(superviseForm.getDepartmentNames()!=null) {
			PersonService personService = (PersonService)getService("personService");
			supervise.setDepartmentSupervisors(ListUtils.join(personService.listOrgSupervisors(superviseForm.getDepartmentIds()), "name", ",", false));
		}
		supervise = (com.yuanluesoft.j2oa.supervise.pojo.Supervise)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//更新完成时限
		SuperviseService superviseService = (SuperviseService)getService("superviseService");
		superviseService.updateTimeLimit(supervise, superviseForm.getLastTimeLimit());
		return supervise;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.workflow.form.WorkflowForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		Supervise superviseForm = (Supervise)workflowForm;
		if("superviseDepartmentLeaders".equals(programmingParticipantId)) { //责任部门领导
			PersonService personService = (PersonService)getService("personService");
			List participants = new ArrayList();
			String[] departmentIds = superviseForm.getDepartmentIds().split(",");
			String[] departmentNames = superviseForm.getDepartmentNames().split(",");
			for(int i=0; i<departmentIds.length; i++) {
				ParticipantDepartment participantDepartment = new ParticipantDepartment(departmentIds[i], departmentNames[i]);
				List orgLeaders = personService.listOrgLeaders(Long.parseLong(departmentIds[i]));
				if(orgLeaders!=null) {
					participantDepartment.setPersonIds(ListUtils.join(orgLeaders, "id", ",", false));
					participantDepartment.setPersonNames(ListUtils.join(orgLeaders, "name", ",", false));
				}
				participants.add(participantDepartment);
			}
			return participants;
		}
		else if("superviseTransactors".equals(programmingParticipantId)) { //经办人
			List participants = new ArrayList();
			String[] transactorIds = superviseForm.getTransactorIds().split(",");
			String[] transactorNames = superviseForm.getTransactorNames().split(",");
			for(int i=0; i<transactorIds.length; i++) {
				participants.add(new Person(transactorIds[i], transactorNames[i]));
			}
			return participants;
		}
		return super.listProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#afterWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
		com.yuanluesoft.j2oa.supervise.pojo.Supervise supervise = (com.yuanluesoft.j2oa.supervise.pojo.Supervise)record;
		if(supervise.getStatus()==SuperviseService.SUPERVISE_STATUS_NEW) {
			supervise.setStatus(SuperviseService.SUPERVISE_STATUS_PROCESSING);
		}
	}
}