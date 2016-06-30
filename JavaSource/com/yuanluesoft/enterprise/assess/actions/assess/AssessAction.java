package com.yuanluesoft.enterprise.assess.actions.assess;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.enterprise.assess.pojo.Assess;
import com.yuanluesoft.enterprise.assess.service.AssessService;
import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.model.WorkflowEntry;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.ActivityEntry;

/**
 * 
 * @author linchuan
 *
 */
public class AssessAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runAssess";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) {
			return;
		}
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowEntry(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, boolean, com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction.WorkflowActionParticipantCallback, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		WorkflowEntry workflowEntry = super.getWorkflowEntry(workflowForm, request, record, openMode, forCreateWorkflowInstance, participantCallback, sessionInfo);
		if(workflowEntry!=null) {
			return workflowEntry;
		}
		WorkflowExploitService workflowExploitService = (WorkflowExploitService)getService("workflowExploitService");
		List wrkflowEntries = workflowExploitService.listWorkflowEntries(workflowForm.getFormDefine().getApplicationName(), participantCallback, sessionInfo);
		if(wrkflowEntries==null || wrkflowEntries.isEmpty()) {
			return null;
		}
		com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry entry = (com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry)wrkflowEntries.get(0);
		return new WorkflowEntry(entry.getWorkflowId(), ((ActivityEntry)entry.getActivityEntries().get(0)).getId());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.enterprise.assess.forms.Assess assessForm = (com.yuanluesoft.enterprise.assess.forms.Assess)workflowForm;
		if(assessForm.getTeamId()==0) {
			return null;
		}
		EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
		EnterpriseProjectTeam team = enterpriseProjectService.getProjectTeam(assessForm.getTeamId());
		return team.listProgrammingParticipants(programmingParticipantId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.enterprise.assess.forms.Assess assessForm = (com.yuanluesoft.enterprise.assess.forms.Assess)form;
		AssessService assessService = (AssessService)getService("assessService");
		//创建一个考核
		Assess assess = assessService.createAssess(assessForm.getTeamId(), sessionInfo);
		PropertyUtils.copyProperties(assessForm, assess);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadPojo(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		if(OPEN_MODE_CREATE.equals(getOpenMode(form, request))) {
			return null;
		}
		AssessService assessService = (AssessService)getService("assessService");
		return assessService.loadAssess(id);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		boolean isNew = OPEN_MODE_CREATE.equals(openMode);
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		AssessService assessService = (AssessService)getService("assessService");
		Assess assess = (Assess)record;
		assessService.saveAssess(assess.getId(), assess.getTeamId(), assess.getWorkflowInstanceId(), isNew, request, sessionInfo);
		return record;
	}
}