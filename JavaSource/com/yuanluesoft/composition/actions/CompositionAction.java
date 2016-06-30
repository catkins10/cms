package com.yuanluesoft.composition.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.composition.forms.Composition;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

public class CompositionAction extends WorkflowAction {

	public String getWorkflowActionName(WorkflowForm workflowForm) {
		// TODO 自动生成方法存根
		return "run";
	}

	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		// TODO 自动生成方法存根
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.initForm(form, acl, sessionInfo, request, response);
		//设置作者信息
		Composition composition=(Composition)form;
		composition.setWriter(sessionInfo.getUserName());
	}
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		com.yuanluesoft.composition.pojo.Composition composition=(com.yuanluesoft.composition.pojo.Composition)record;
		if(OPEN_MODE_CREATE.equals(openMode)){
			composition.setWriter(sessionInfo.getUserName());
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Composition composition=(Composition)form;
		//设置意见
		getOpinionService().fillOpinionPackageByWorkItemId(composition.getOpinionPackage(), composition.getOpinions(), form.getFormDefine().getRecordClassName(), composition.getWorkItemId(), composition.getParticipantId(), sessionInfo);
	}
}
