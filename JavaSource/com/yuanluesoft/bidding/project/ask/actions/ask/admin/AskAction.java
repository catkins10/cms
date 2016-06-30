package com.yuanluesoft.bidding.project.ask.actions.ask.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bidding.project.ask.forms.admin.Ask;
import com.yuanluesoft.bidding.project.ask.pojo.BiddingProjectAsk;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectAgent;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.user.ParticipantDepartment;

/**
 * 
 * @author linchuan
 *
 */
public class AskAction extends WorkflowAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runAsk";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#listProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		ParticipantDepartment participantDepartment = new ParticipantDepartment();
		Ask askForm = (Ask)workflowForm;
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		BiddingProject project = biddingProjectService.getProject(askForm.getProjectId());
		if(project.getAgentEnable().equals("否")) { //自行招标
			participantDepartment.setId(project.getOwnerId() + "");
			participantDepartment.setName(project.getOwner());
		}
		else { //代理招标
			BiddingProjectAgent agent = (BiddingProjectAgent)project.getBiddingAgents().iterator().next();
			participantDepartment.setId(agent.getAgentId() + "");
			participantDepartment.setName(agent.getAgentName());
		}
		ArrayList participants = new ArrayList();
		participants.add(participantDepartment);
		return participants;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		BiddingProjectAsk ask = (BiddingProjectAsk)record;
		if(ask!=null && ask.getIsPublic()=='1') { //已公开
			FormAction formAction = (FormAction)ListUtils.findObjectByProperty(form.getFormActions(), "title", "向所有人公开");
			if(formAction!=null) {
				formAction.setTitle("撤销公开");
				formAction.setExecute("FormUtils.doAction('publicAsk', 'isPublic=0')");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //允许所有企业提问
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(acl.contains("application_manager")) { //管理员
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		BiddingProjectAsk ask = (BiddingProjectAsk)record;
		if(ask.getWorkflowInstanceId()==null || ask.getWorkflowInstanceId().isEmpty()) {
			//检查是否公开
			if(ask.getIsPublic()=='1') { //已经公开
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
			throw new PrivilegeException();
		}
		try {
			return super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
		}
		catch(PrivilegeException pe) {
			//检查是否公开
			if(ask.getIsPublic()=='1') { //已经公开
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
			throw pe;
		}
	}
}