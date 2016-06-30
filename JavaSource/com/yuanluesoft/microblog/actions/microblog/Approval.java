package com.yuanluesoft.microblog.actions.microblog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Approval extends MicroblogAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, "审核完成", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeApproval(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeApproval(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		super.beforeApproval(workflowForm, request, record, openMode, sessionInfo);
		workflowForm.setWorkflowApprovalDialogTitle("审核");
		workflowForm.setWorkflowApprovalOptions("同意发布,不同意发布");
	}
}