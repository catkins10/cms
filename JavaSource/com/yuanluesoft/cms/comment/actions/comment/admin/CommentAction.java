package com.yuanluesoft.cms.comment.actions.comment.admin;

import com.yuanluesoft.cms.publicservice.actions.PublicServiceAdminAction;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class CommentAction extends PublicServiceAdminAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#getWorkflowActionName()
	 */
	public String getWorkflowActionName(WorkflowForm workflowForm) {
		return "runComment";
	}
}
