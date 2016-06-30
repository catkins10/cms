package com.yuanluesoft.logistics.complaint.actions.complaint.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.logistics.complaint.forms.admin.Complaint;
import com.yuanluesoft.logistics.complaint.pojo.LogisticsComplaint;
import com.yuanluesoft.logistics.complaint.service.LogisticsComplaintService;

/**
 * 
 * @author linchuan
 *
 */
public class ComplaintAction extends PublicServiceAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runComplaint";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		LogisticsComplaint complaint = (LogisticsComplaint)record;
		Complaint complaintForm = (Complaint)form;
		LogisticsComplaintService logisticsComplaintService = (LogisticsComplaintService)getService("logisticsComplaintService");
		//统计货源/车源被投诉次数
		complaintForm.setSupplyComplaintTimes(logisticsComplaintService.countSupplyComplaintTimes(complaint));
		//统计公司/用户被投诉次数
		complaintForm.setUserComplaintTimes(logisticsComplaintService.countUserComplaintTimes(complaint));
	}
}