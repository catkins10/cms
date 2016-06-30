package com.yuanluesoft.cms.onlineservice.interactive.complaint.actions.complaint.admin;

import com.yuanluesoft.cms.onlineservice.interactive.actions.OnlineServiceInteractiveAdminAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class ComplaintAction extends OnlineServiceInteractiveAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runComplaint";
	}
}