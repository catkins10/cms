/*
 * Created on 2005-10-16
 *
 */
package com.yuanluesoft.j2oa.dispatch.actions.dispatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.dispatch.pojo.Dispatch;
import com.yuanluesoft.j2oa.dispatch.service.DispatchService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;

/**
 *
 * @author linchuan1
 *
 */
public class Distribute extends DispatchAction {
    
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return executeSaveAction(mapping, form, request, response, false, null, "分发完成！", null);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.j2oa.dispatch.actions.dispatch.DispatchAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		Dispatch dispatch = (Dispatch)record;
		com.yuanluesoft.j2oa.dispatch.forms.Dispatch dispatchForm = (com.yuanluesoft.j2oa.dispatch.forms.Dispatch)form;
		if(!dispatchForm.isWorkflowTest()) { //不是测试状态
			DispatchService dispatchService = (DispatchService)getService("dispatchService");
			dispatchService.distribute(dispatch, dispatchForm.getWorkItemId(), sessionInfo);
		}
		WorkflowExploitService workflowExploitService = getWorkflowExploitService();
		workflowExploitService.completeAction(dispatchForm.getWorkflowInstanceId(), dispatchForm.getWorkItemId(), "分发", getWorkflowSessionInfo(dispatchForm, sessionInfo));
		return record;
	}
}