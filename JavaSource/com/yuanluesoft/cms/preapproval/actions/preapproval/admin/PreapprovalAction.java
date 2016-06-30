/**
 * 
 */
package com.yuanluesoft.cms.preapproval.actions.preapproval.admin;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 *
 * @author LinChuan
 *
 */
public class PreapprovalAction extends PublicServiceAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runPreapproval";
	}
}