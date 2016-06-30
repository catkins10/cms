package com.yuanluesoft.enterprise.workreport.actions.workreport;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.enterprise.project.pojo.EnterpriseProjectTeam;
import com.yuanluesoft.enterprise.project.service.EnterpriseProjectService;
import com.yuanluesoft.enterprise.workreport.pojo.WorkReport;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
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
public class WorkReportAction extends WorkflowAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runWorkReport";
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
		com.yuanluesoft.enterprise.workreport.forms.WorkReport workReportForm = (com.yuanluesoft.enterprise.workreport.forms.WorkReport)workflowForm;
		if(workReportForm.getTeamId()==0) {
			return null;
		}
		EnterpriseProjectService enterpriseProjectService = (EnterpriseProjectService)getService("enterpriseProjectService");
		EnterpriseProjectTeam team = enterpriseProjectService.getProjectTeam(workReportForm.getTeamId());
		return team.listProgrammingParticipants(programmingParticipantId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.enterprise.workreport.forms.WorkReport workReportForm = (com.yuanluesoft.enterprise.workreport.forms.WorkReport)form;
		workReportForm.setReporterName(sessionInfo.getUserName()); //汇报人
		workReportForm.setReportTime(DateTimeUtils.now()); //汇报时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			WorkReport workReport = (WorkReport)record;
			workReport.setReporterId(sessionInfo.getUserId()); //汇报人ID
			workReport.setReporterName(sessionInfo.getUserName()); //汇报人姓名
			workReport.setReportTime(DateTimeUtils.now()); //汇报时间
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}