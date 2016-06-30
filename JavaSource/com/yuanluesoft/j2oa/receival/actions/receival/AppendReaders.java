package com.yuanluesoft.j2oa.receival.actions.receival;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.receival.forms.Receival;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 * 
 * @author LinChuan
 *
 */
public class AppendReaders extends ReceivalAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSaveAction(mapping, form, request, response, true, null, null, null);
    }
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.receival.actions.receival.ReceivalAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		com.yuanluesoft.j2oa.receival.pojo.Receival receival = (com.yuanluesoft.j2oa.receival.pojo.Receival)record;
		Receival formReceival = (Receival)form;
		WorkflowExploitService workflowExploitService = (WorkflowExploitService)getService("workflowExploitService");
		String appendedNames = null;
		String[] ids = formReceival.getAppendReaderIds().split(",");
		String[] names = formReceival.getAppendReaderNames().split(",");
		//String message = "请查阅收文:" + formReceival.getSubject();
		WorkflowMessage workflowMessage = createWorklfowMessage(record, formReceival);
		workflowMessage.setContent("请查阅收文:"  + workflowMessage.getContent());
		for(int i=0; i<ids.length; i++) {
			if(workflowExploitService.addVisitor(formReceival.getWorkflowInstanceId(), formReceival.getWorkItemId(), ids[i], names[i], workflowMessage, receival, sessionInfo)) {
				appendedNames = (appendedNames==null ? "" : appendedNames + ",") + names[i];
			}
		}
		if(appendedNames!=null) {
			workflowExploitService.writeTransactLog(formReceival.getWorkflowInstanceId(), formReceival.getWorkItemId(), "增加传阅人:" + appendedNames, sessionInfo);
		}
		return record;
    }
}