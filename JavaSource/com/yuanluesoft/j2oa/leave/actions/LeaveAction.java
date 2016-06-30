/*
 * Created on 2006-7-3
 *
 */
package com.yuanluesoft.j2oa.leave.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.leave.forms.LeaveForm;
import com.yuanluesoft.j2oa.leave.pojo.Leave;
import com.yuanluesoft.j2oa.leave.service.LeaveService;
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
public class LeaveAction extends WorkflowAction {
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "run";
	}
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
	     Leave leave = (Leave)record;
        //设置申请人信息
        if(OPEN_MODE_CREATE.equals(openMode)) {
        	leave.setPersonId(sessionInfo.getUserId());
        	leave.setPersonName(sessionInfo.getUserName());
        }
        super.saveRecord(form, record, openMode, request, response, sessionInfo);
        //保存代理人
        LeaveForm leaveForm = (LeaveForm)form;
		if(leaveForm.getAgents().getVisitorIds()!=null) { //提交的内容中必须包含代理人
			getRecordControlService().updateVisitors(leaveForm.getId(), Leave.class.getName(), leaveForm.getAgents(), RecordControlService.ACCESS_LEVEL_PREREAD);
		}
		return record;
    }
	
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
        LeaveForm leaveForm = (LeaveForm)form;
        //设置代理人
		RecordVisitorList visitors = getRecordControlService().getVisitors(leaveForm.getId(), Leave.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(visitors!=null) {
		    leaveForm.setAgents(visitors);
		}
		//设置意见
        getOpinionService().fillOpinionPackageByWorkItemId(leaveForm.getOpinionPackage(), leaveForm.getOpinions(), form.getFormDefine().getRecordClassName(), leaveForm.getWorkItemId(), leaveForm.getParticipantId(), sessionInfo);
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
    public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
        LeaveForm leaveForm = (LeaveForm)form;
        //设置申请人信息
        leaveForm.setPersonId(sessionInfo.getUserId());
        leaveForm.setPersonName(sessionInfo.getUserName());
        //设置部门信息
        if(leaveForm.getDepartmentId()==0) {
	        leaveForm.setDepartmentId(sessionInfo.getDepartmentId());
	        leaveForm.setDepartmentName(sessionInfo.getDepartmentName());
        }
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#afterApproval(java.lang.String, com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void afterApproval(String approvalResult, WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		super.afterApproval(approvalResult, workflowForm, request, record, openMode, sessionInfo);
		Leave leave = (Leave)record;
        LeaveService leaveService = (LeaveService)getService("leaveService");
        leaveService.approvalLeave(leave, "同意".equals(approvalResult));
	}
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#createWorklfowMessage(java.lang.Object)
     */
    protected WorkflowMessage createWorklfowMessage(Record record, WorkflowForm workflowForm) throws Exception {
        WorkflowMessage workflowMessage = super.createWorklfowMessage(record, workflowForm);
        Leave leave = (Leave)record;
        workflowMessage.setContent(leave.getPersonName() + "," + leave.getReason() + "(请假" + leave.getDayCount() + "天)");
        return workflowMessage;
    }
}