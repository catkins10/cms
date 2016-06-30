package com.yuanluesoft.dpc.keyproject.actions.project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProject;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class SubmitDebrief extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, false, "汇报完成", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkflowRun(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected void beforeWorkflowRun(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, Record record, SessionInfo sessionInfo) throws Exception {
		super.beforeWorkflowRun(workflowForm, request, response, record, sessionInfo);
		KeyProject project = (KeyProject)record;
		project.setDeclareOrDebrief("汇报");
	}
}