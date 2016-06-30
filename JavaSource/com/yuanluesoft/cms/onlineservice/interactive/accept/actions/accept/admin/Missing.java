package com.yuanluesoft.cms.onlineservice.interactive.accept.actions.accept.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.interactive.accept.forms.admin.Accept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.service.OnlineServiceAcceptService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Missing extends AcceptAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Accept acceptForm = (Accept)form;
    	acceptForm.setWorkflowAction(WORKFLOW_ACTION_SELECT_REVERSE_INSTANCE);
    	return executeRunAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#runDoReverse(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void runDoReverse(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Accept acceptForm = (Accept)workflowForm;
		OnlineServiceAcceptService onlineServiceAcceptService = (OnlineServiceAcceptService)getService("onlineServiceAcceptService");
		//保存缺件通知
		onlineServiceAcceptService.sendMissingNotify(acceptForm.getMissing().getDescription(), acceptForm.getId(), sessionInfo);
		super.runDoReverse(workflowForm, record, request, response, sessionInfo);
	}
}