package com.yuanluesoft.cms.onlineservice.interactive.consult.actions.consult.admin;

import com.yuanluesoft.cms.onlineservice.interactive.actions.OnlineServiceInteractiveAdminAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class ConsultAction extends OnlineServiceInteractiveAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runConsult";
	}
}