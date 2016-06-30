package com.yuanluesoft.enterprise.iso.actions.document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.iso.pojo.IsoDocument;
import com.yuanluesoft.enterprise.iso.service.IsoDocumentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class CompleteCreate extends DocumentAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, null, null);
    }
	
    /*
     * (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, boolean, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
     */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		IsoDocument document = (IsoDocument)record;
		IsoDocumentService isoDocumentService = (IsoDocumentService)getService("isoDocumentService");
		isoDocumentService.completeCreate(document, sessionInfo);
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}