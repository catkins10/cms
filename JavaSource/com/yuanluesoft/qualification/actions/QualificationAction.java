package com.yuanluesoft.qualification.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.qualification.forms.Qualification;
import com.yuanluesoft.qualification.service.QualificationService;

public class QualificationAction extends WorkflowAction {
	
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		// TODO 自动生成方法存根
		return "run";
	}
	
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		// TODO 自动生成方法存根
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Qualification qualification=(Qualification)form;
		//设置意见
		getOpinionService().fillOpinionPackageByWorkItemId(qualification.getOpinionPackage(), qualification.getOpinions(), form.getFormDefine().getRecordClassName(), qualification.getWorkItemId(), qualification.getParticipantId(), sessionInfo);
	}
	protected void afterApproval(String approvalResult, WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		super.afterApproval(approvalResult, workflowForm, request, record, openMode,
				sessionInfo);
		com.yuanluesoft.qualification.pojo.Qualification qualification=(com.yuanluesoft.qualification.pojo.Qualification)record;
		QualificationService qualificationService=(QualificationService)getService("qualificationService");
		qualificationService.approvalQualification(qualification, "同意".equals(approvalResult));
	}
}
