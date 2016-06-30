/*
 * Created on 2006-6-28
 *
 */
package com.yuanluesoft.j2oa.businesstrip.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.businesstrip.forms.BusinesstripForm;
import com.yuanluesoft.j2oa.businesstrip.pojo.Businesstrip;
import com.yuanluesoft.j2oa.businesstrip.service.BusinesstripService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.actions.WorkflowAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 *
 * @author linchuan
 *
 */
public class BusinesstripAction extends WorkflowAction {

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "run";
	}
	
	/* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
        BusinesstripForm businessTripForm = (BusinesstripForm)form;
        //设置出差人
		RecordVisitorList visitors = getRecordControlService().getVisitors(businessTripForm.getId(), Businesstrip.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(visitors!=null) {
		    businessTripForm.setTripPerson(visitors);
		}
		//设置意见
        getOpinionService().fillOpinionPackageByWorkItemId(businessTripForm.getOpinionPackage(), businessTripForm.getOpinions(), form.getFormDefine().getRecordClassName(), businessTripForm.getWorkItemId(), businessTripForm.getParticipantId(), sessionInfo);
    }

	/* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
        BusinesstripForm businessTripForm = (BusinesstripForm)form;
        //设置申请人信息
        businessTripForm.setProposerId(sessionInfo.getUserId());
        businessTripForm.setProposerName(sessionInfo.getUserName());
        //设置部门信息
        if(businessTripForm.getDepartmentId()==0) {
	        businessTripForm.setDepartmentId(sessionInfo.getDepartmentId());
	        businessTripForm.setDepartmentName(sessionInfo.getDepartmentName());
        }
        //设置当前用户为出差人
        businessTripForm.getTripPerson().setVisitorIds("" + sessionInfo.getUserId());
        businessTripForm.getTripPerson().setVisitorNames(sessionInfo.getUserName());
    }

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
    public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
        Businesstrip businessTrip = (Businesstrip)record;
        //设置申请人信息
        businessTrip.setProposerId(sessionInfo.getUserId());
        businessTrip.setProposerName(sessionInfo.getUserName());
        super.saveRecord(form, record, openMode, request, response, sessionInfo);
        //保存出差人
        BusinesstripForm businessTripForm = (BusinesstripForm)form;
		if(businessTripForm.getTripPerson().getVisitorIds()!=null) { //提交的内容中必须包含分发范围
			getRecordControlService().updateVisitors(businessTrip.getId(), Businesstrip.class.getName(), businessTripForm.getTripPerson(), RecordControlService.ACCESS_LEVEL_PREREAD);
		}
		return record;
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.workflow.form.WorkflowForm, boolean, boolean, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
		if(workflowForm.getWorkflowApprovalResult()!=null && !workflowForm.getWorkflowApprovalResult().isEmpty()) { //审批
			Businesstrip businessTrip = (Businesstrip)record;
			BusinesstripService businesstripService = (BusinesstripService)getService("businesstripService");
			businesstripService.approvalBusinesstrip(businessTrip, workflowForm.getWorkItemId(), "同意".equals(workflowForm.getWorkflowApprovalResult()), sessionInfo);
		}
	}

	/* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#createWorklfowMessage(java.lang.Object)
     */
    protected WorkflowMessage createWorklfowMessage(Record record, WorkflowForm workflowForm) throws Exception {
        WorkflowMessage workflowMessage = super.createWorklfowMessage(record, workflowForm);
        Businesstrip businessTrip = (Businesstrip)record;
        workflowMessage.setContent(businessTrip.getReason() + "(" + businessTrip.getAddress() + ")");
        return workflowMessage;
    }
}